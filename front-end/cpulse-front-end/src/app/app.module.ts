import { NgModule, inject } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignupComponent } from './components/signup/signup.component';
import { HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { InterceptorService } from './interceptors/interceptor.service';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { AdminDashboardProductsComponent } from './components/admin-dashboard-products/admin-dashboard-products.component';
import { AdminDashboardUsersComponent } from './components/admin-dashboard-users/admin-dashboard-users.component';
import { CartComponent } from './components/cart/cart.component';
import { LoginComponent } from './components/login/login.component';
import { OrdersComponent } from './components/orders/orders.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ProductsComponent } from './components/products/products.component';
import { ProfileComponent } from './components/profile/profile.component';
import { WishlistComponent } from './components/wishlist/wishlist.component';
import { CanActivateFn, Router, RouterModule, Routes } from '@angular/router';
import { DbService } from './services/db.service';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap'; 
import { catchError, map, of } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { OrderComponent } from './components/order/order.component';
import { AdminDashboardPaymentsComponent } from './components/admin-dashboard-payments/admin-dashboard-payments.component';

const adminAuthGuard: CanActivateFn = (route, state) => {
  const dbService = inject(DbService);
  const router = inject(Router);
  return dbService.verifyAdminLoggedIn().pipe(
    map(isAdmin => {
      if (isAdmin === 1) {
        return true; 
      } else {
        return false;
      }
    }),
    catchError(error => {
      if (error.status === 403) {
        router.navigate(['/login']);
      }
      return of(false); 
    })
  );
};

const userAuthGuard: CanActivateFn = (route, state) => {
  const dbService = inject(DbService);
  const router = inject(Router);
  return dbService.verifyUserLoggedIn().pipe(
    map(isUser => {
      if (isUser === 1) {
        return true; 
      } else {
        return false; 
      }
    }),
    catchError(error => {
      if (error.status === 403) {
        router.navigate(['/login']);
      }
      return of(false); 
    })
  );
};


const routes: Routes = [
  {path: "admin-dashboard", component: AdminDashboardComponent, canActivate: [adminAuthGuard]},
  {path: "admin-dashboard-products", component: AdminDashboardProductsComponent, canActivate: [adminAuthGuard]},
  {path: "admin-dashboard-users", component: AdminDashboardUsersComponent, canActivate: [adminAuthGuard]},
  {path: "admin-dashboard-payments", component: AdminDashboardPaymentsComponent, canActivate: [adminAuthGuard]},
  {path: "cart", component: CartComponent, canActivate: [userAuthGuard]},
  {path: "login", component: LoginComponent},
  {path: "orders", component: OrdersComponent, canActivate: [userAuthGuard]},
  {path: "product-details/:productName", component: ProductDetailsComponent},
  {path: "products", component: ProductsComponent},
  {path: "profile", component: ProfileComponent, canActivate: [userAuthGuard]},
  {path: "signup", component: SignupComponent},
  {path: "wishlist", component: WishlistComponent},
  {path: "order", component: OrderComponent},
  {path: '', redirectTo: '/products', pathMatch: 'full'},
  {path: '**', redirectTo: '/products', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    AdminDashboardComponent,
    AdminDashboardProductsComponent,
    AdminDashboardUsersComponent,
    CartComponent,
    LoginComponent,
    OrdersComponent,
    ProductDetailsComponent,
    ProductsComponent,
    ProfileComponent,
    WishlistComponent,
    OrderComponent,
    AdminDashboardPaymentsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbPaginationModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    FormsModule 
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: InterceptorService,
      multi: true,
    }
  ],
  bootstrap: [AppComponent],
  exports: [
    RouterModule
  ]
})
export class AppModule { }
