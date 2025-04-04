import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Product } from 'src/app/entities/product';
import { WishlistItem } from 'src/app/entities/wishlist-item';
import { DbService } from 'src/app/services/db.service';

type ProductOrWishlistItemArrayType = Observable<Product[]> | Observable<WishlistItem[]>;

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent {
  products: Product[] = [];
  productNames: string[] = [];
  isUserAuthenticated: number = 0;
  isAdminAuthenticated: number = 0; 
  username: string | null = null;

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  ngOnInit(){
    this.verifyUserAuthenticated().subscribe({
      next: response => {
        this.isUserAuthenticated = response;

        this.findAllProductsFromWishlist().subscribe({
          next: result => {
            this.products = result;
          }
        })
      }
    });

    this.verifyAdminAuthenticated().subscribe({
      next: response => {
        this.isAdminAuthenticated = response;
        
        this.findAllProductsFromWishlist().subscribe({
          next: result => {
            this.products = result;
          }
        })
      }
    });

    this.findAllProductsFromWishlist().subscribe({
      next: theProducts => {
        this.products = theProducts;
      }
    });
    
  }

  findAllProductsFromWishlist(): Observable<Product[]> {
    if(!this.isUserAuthenticated && !this.isAdminAuthenticated){
      return of(this.dbService.getUserNotLoggedInWishlistItems());
    }else{
      return this.dbService.findAllUserWishlistProducts();
    }
  }

  deleteWishlistItem(productName: string){
    if(!this.isUserAuthenticated && !this.isAdminAuthenticated){
      this.dbService.deleteProductFromPulseWishlist(productName);
      console.log(document.cookie);
    }else{
      this.dbService.deleteUserLoggedInWishlistItem(productName);
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
}
