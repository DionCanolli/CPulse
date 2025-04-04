import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
  products: Product[] = [];
  username: string | null = null;
  totalPrice: number = 0;

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  ngOnInit(){
    this.findAllProductsFromCart().subscribe({
      next: theProducts => {
        this.products = theProducts;
        this.findTotalPrice().subscribe({
          next: totalPrice => {
            this.totalPrice = totalPrice;
          }
        });
      }
    });

    
  }

  findTotalPrice(): Observable<number>{
    let total: number = 0;
    for(let product of this.products){
      total += product.productPrice!;
    } 
    return of(total);
  }

  findAllProductsFromCart(): Observable<Product[]> {
    return this.dbService.findAllCartItemsByUser(); 
  }

  deleteCartItem(productName: string) {
    this.dbService.deleteCartItem(productName).subscribe({
        next: (response) => {
          this.products = this.products.filter(p => p.productName !== productName);
        },
        error: (error) => {
            console.error('Error deleting cart item:', error);
        }
    });
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

  viewWishlist() {
    this.router.navigate(['/wishlist']);
  }

  logout() {
    this.dbService.logout();
    this.router.navigate(['/products']);
    window.location.reload();
  }

  goToOrder(){
    let products = this.products;
    this.router.navigateByUrl('/order', { state: { products, isSpecialOrder: false} });
  }
}
