import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { PaginatedProducts } from 'src/app/entities/paginated-products';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {
  
  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  paginatedProducts: PaginatedProducts | null = null;
  products: Product[] | undefined= [];
  categoryName: string | null = null;
  productName: string | null = null;
  pageNumber: number = 1;
  nrProductsReturned: number | undefined = 0;
  username: string | null = null;
  isUserAuthenticated: number = 0;
  isAdminAuthenticated: number = 0; 

  ngOnInit(): void{
    this.listAllProducts(this.categoryName, this.productName, this.pageNumber-1);
    this.verifyUserAuthenticated().subscribe({
      next: response => {
        this.isUserAuthenticated = response;
      }
    });

    this.verifyAdminAuthenticated().subscribe({
      next: response => {
        this.isAdminAuthenticated = response;
      }
    });   
  }

  listAllProducts(categoryName: string | null, productName: string | null, page: number): void{
    this.pageNumber = page;
    this.categoryName = categoryName;
    this.productName = productName;
    const backendPageNumber = page - 1; 
    this.dbService.findAllProducts(this.categoryName, this.productName, backendPageNumber).subscribe({
        next: response => {
          this.paginatedProducts = response;
          this.products = response.allProducts;
          this.nrProductsReturned = response.totalProducts
        }
      } 
    )
  }

  verifyUserAuthenticated(): Observable<number> {
    return this.dbService.verifyUserLoggedIn();
  }

  verifyAdminAuthenticated(): Observable<number> {
    return this.dbService.verifyAdminLoggedIn();
  }

  toggleHiddenMenu() {
    const hiddenMenu = document.getElementById('hiddenMenu');
    if (hiddenMenu) {
      hiddenMenu.classList.toggle('active');
    } else {
      console.error("Element with ID 'hiddenMenu' not found");
    }
  }

  toggleCategoryMenu(): void {
    const categoryMenu = document.getElementById('categoryMenu');
    if (categoryMenu) {
      categoryMenu.classList.toggle('active');
    } else {
      console.error("Element with ID 'categoryMenu' not found");
    }
  }

  viewProduct(productName: string | undefined) {
    this.router.navigate(['/product-details', productName]);
  }

  viewWishlist() {
      this.router.navigate(['/wishlist']);
  }

  viewProfile() {
    this.router.navigate(['/profile']);
  }

  viewOrders() {
    this.router.navigate(['/orders']);
  }

  viewCart() {
    this.router.navigate(['/cart']);
  }

  viewLogin() {
      this.router.navigate(['/login']);
  }
  
  viewSignup() {
    this.router.navigate(['/signup']);
  }

  viewProducts() {
    this.router.navigate(['/products']);
  }
  
  logout() {
    this.dbService.logout();
    this.router.navigate(['/products']);
    window.location.reload();
  }
}
