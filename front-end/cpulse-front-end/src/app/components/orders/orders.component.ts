import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { format } from 'date-fns';
import { Observable } from 'rxjs';
import { Order } from 'src/app/entities/order';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent {

  username: string | null = null;
  orders: Order[] = [];

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  ngOnInit(){
    this.findUserPayments().subscribe({
      next: orders => {
        this.orders = orders.map(order => new Order(order.amount, new Date(order.paymentDate!)));
      }
    });
  }

  findUserPayments(): Observable<Order[]> {
    return this.dbService.findUserPayments();
  }

  formatDate(date: Date): string {
    return format(date, 'yyyy-MM-dd HH:mm:ss');
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

  viewCart() {
    this.router.navigate(['/cart']);
  }

  logout() {
    this.dbService.logout();
    this.router.navigate(['/login']);
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
}
