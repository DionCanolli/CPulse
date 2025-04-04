import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Observable, of } from 'rxjs';
import { PaginatedProducts } from 'src/app/entities/paginated-products';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent {

  isUserAuthenticated: number = 0;
  isAdminAuthenticated: number = 0; 
  username: string | null = null;
  productName: string = this.activatedRoute.snapshot.paramMap.get('productName')!;
  products: PaginatedProducts | null = null;
  product: Product | null = null;
  productExistsInWishlistList: boolean | Boolean = false;
  productExistsInCartList: boolean | Boolean = false;

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}
  
  ngOnInit(): void{
    this.dbService.findAllProducts(null, this.productName, 0).subscribe({
      next: (response) => {
        if(response.allProducts != undefined){
          this.product = response.allProducts[0];
        }
      }
    });
  
    this.verifyUserAuthenticated().subscribe({
      next: response => {
        this.isUserAuthenticated = response;

        this.productExistsInWishlist().subscribe({
          next: result => {
            this.productExistsInWishlistList = result;
          }
        });

        if(response === 1){
          this.productExistsInCart().subscribe({
            next: result => {
              this.productExistsInCartList = result;
            }
          });
        }
      }
    });

    this.verifyAdminAuthenticated().subscribe({
      next: response => {
        this.isAdminAuthenticated = response;
        
        this.productExistsInWishlist().subscribe({
          next: result => {
            this.productExistsInWishlistList = result;
          }
        });

        if(response === 1){
          this.productExistsInCart().subscribe({
            next: result => {
              this.productExistsInCartList = result;
            }
          });
        }
      }
    });

    this.productExistsInWishlist().subscribe({
      next: result => {
        this.productExistsInWishlistList = result;
      }
    });
  }

  insertProductIntoWishlist(){
    if(this.isUserAuthenticated == 0 && this.isAdminAuthenticated == 0){
      this.dbService.insertUserNotLoggedInWishlistItem(this.productName).subscribe({
        next: response => {
          this.productExistsInWishlistList = response;
        }
      });
    }else{
      this.dbService.insertUserLoggedInWishlistItem(this.productName).subscribe({
        next: response => {
          this.productExistsInWishlistList = response;
        }
      });
    }
  }

  productExistsInWishlist(): Observable<boolean>{
    if(this.isUserAuthenticated == 0 && this.isAdminAuthenticated == 0){
      return of(this.dbService.productNameExistsUserNotLoggedInWishlist(this.productName));
    }else{
      return this.dbService.wishlistItemExistsUserLoggedin(this.productName);
    }
  }

  productExistsInCart(): Observable<boolean>{
    return this.dbService.cartItemExistsUserLoggedin(this.productName);
  }

  insertProductIntoCart(productName: string){
    if(this.product!.productStockQuantity! > 0){
      this.dbService.insertCartItem(productName).subscribe({
        next: response => {
          this.productExistsInCartList = response;
        }
      });
    }
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

  viewProducts() {
    this.router.navigate(['/products']);
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

  logout() {
    this.dbService.logout();
    this.router.navigate(['/login']);
  }
  
  viewSignup() {
    this.router.navigate(['/signup']);
  }

  goToOrder(){
    let products: Product[] = [];
    products[0] = this.product!;
    this.router.navigateByUrl('/order', { state: { products, isSpecialOrder: true} });
  }
}
