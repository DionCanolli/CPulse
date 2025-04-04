import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of, switchMap, throwError } from 'rxjs';
import { User } from '../entities/user';
import { Product } from '../entities/product';
import { Category } from '../entities/category';
import { WishlistItem } from '../entities/wishlist-item';
import { Payment } from '../entities/payment';
import { PaginatedProducts } from '../entities/paginated-products';
import { Order } from '../entities/order';
import { FullPayment } from '../entities/full-payment';

@Injectable({
  providedIn: 'root'
})
export class DbService {

  private baseUrl: string = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  /* --------------------- Permitted requests ----------------------*/
  
  public signUp(user: User): Observable<boolean>{
    const signUpUrl = this.baseUrl + "/permitted/users/signup";
    return this.httpClient.post<boolean>(signUpUrl, user);
  }

  public login(user: User): Observable<{ token: string }> {
    const signUpUrl = this.baseUrl + "/permitted/users/login";
    return this.httpClient.post<{ token: string }>(signUpUrl, user, { observe: 'response' }).pipe(
      map(response => {
        if (response.status === 200) {
          const token = response.body!.token; 
          localStorage.setItem('jwtToken', token); 
          return { token }; 
        }
        throw new Error('Unexpected response status or missing body'); // Handle unexpected cases
      })
    );
  }

  public findAllProducts(categoryName?: string | null, productName?: string | null, page?: number): Observable<PaginatedProducts>{
    let findAllProductsUrl: string = "";
    if(categoryName != null && productName != null){
      findAllProductsUrl = this.baseUrl + `/permitted/products/find?categoryName=${categoryName}
          &productName=${productName}&page=${page}`;
    } else if(categoryName == null && productName != null){
      findAllProductsUrl = this.baseUrl + `/permitted/products/find?productName=${productName}&page=${page}`;
    }else if(categoryName != null && productName == null){
      findAllProductsUrl = this.baseUrl + `/permitted/products/find?categoryName=${categoryName}&page=${page}`;
    }else if(categoryName == null && productName == null){
      findAllProductsUrl = this.baseUrl + `/permitted/products/find?page=${page}`;
    }
    return this.httpClient.get<PaginatedProducts>(findAllProductsUrl);
  }
  
  public findAllCategories(): Observable<Category[]>{
    const findAllCategoriesUrl: string = this.baseUrl + `/permitted/categories/all`;
    return this.httpClient.get<Category[]>(findAllCategoriesUrl);
  }

  public findAllReviewsByProduct(productName: string): Observable<number>{
    const findAllReviewsByProductUrl: string = this.baseUrl + 
        `/permitted/ratings/product?productName=${productName}`;
    return this.httpClient.get<number>(findAllReviewsByProductUrl);
  }
  
  public modifyProduct(productName: string, quantity: number): Observable<boolean>{
    const updateProductUrl: string = this.baseUrl +
       `/permitted/products/update?productName=${productName}&quantity=${quantity}`;
    return this.httpClient.put<boolean>(updateProductUrl, {});
  }
  /* --------------------- User requests ----------------------*/
  
  public findAllUsers(): Observable<User[]> {
    const findAllUsersUrl: string = this.baseUrl + "/admin/users/all";
    return this.httpClient.get<User[]>(findAllUsersUrl);
  } 

  public verifyAdminLoggedIn(): Observable<number> {
    const verifyAdminLoggedInUrl: string = this.baseUrl + "/admin/verify";
    return this.httpClient.get<number>(verifyAdminLoggedInUrl);
  } 

  public verifyUserLoggedIn(): Observable<number> {
    const verifyUserLoggedInUrl: string = this.baseUrl + "/users/verify";
    return this.httpClient.get<number>(verifyUserLoggedInUrl);
  } 

  public findUser(): Observable<User> {
    const findUserUrl: string = this.baseUrl + "/users/one";
    return this.httpClient.get<User>(findUserUrl);
  } 

  public updateUser(user: User): Observable<boolean>{
    const updateUserUrl: string = this.baseUrl + "/users/update";
    return this.httpClient.put<boolean>(updateUserUrl, user);
  }
  
  public deleteUser(): Observable<boolean>{
    const deleteUserUrl: string = this.baseUrl + "/user/delete";
    return this.httpClient.delete<boolean>(deleteUserUrl);
  }

  public logout(): Observable<boolean>{
    localStorage.removeItem('jwtToken');
    const deleteUserUrl: string = this.baseUrl + "/users/logout";
    return this.httpClient.post<boolean>(deleteUserUrl, {});
  }
  
  /* --------------------- Product requests ----------------------*/

  insertProduct(productDTO: any, productImage: File): Observable<boolean> {
    console.log(productDTO);

    const formData = new FormData();
  
    // Append the productDTO as a JSON string
    formData.append('productDTO', new Blob([JSON.stringify(productDTO)], { type: 'application/json' }));
  
    // Append the productImage file
    formData.append('productImage', productImage);
  
    return this.httpClient.post<boolean>(`${this.baseUrl}/admin/products/insert`, formData);
  }


  public deleteProduct(productName: string): Observable<boolean>{
    const deleteProductUrl: string = this.baseUrl + "/admin/products/delete?productName=" + productName;  
    return this.httpClient.delete<boolean>(deleteProductUrl);
  }
  public findAllProductsNotPaginated(): Observable<Product[]>{
    const findAllProductsNotPaginatedUrl: string = this.baseUrl + "/permitted/products/all";  
    return this.httpClient.get<Product[]>(findAllProductsNotPaginatedUrl);
  }

  /* --------------------- Wishlist requests ----------------------*/

  public findAllUserWishlistProducts(): Observable<WishlistItem[]>{
    const findAllUserWishlistProductsUrl = this.baseUrl + "/wishlist/user";
    return this.httpClient.get<Product[]>(findAllUserWishlistProductsUrl);
  }

  public insertUserLoggedInWishlistItem(productName: string): Observable<Boolean> {
    const insertUserLoggedInWishlistItemUrl: string = this.baseUrl + `/wishlist/insert?productName=${productName}`;
    return this.httpClient.post<Boolean>(insertUserLoggedInWishlistItemUrl, {}).pipe(
      catchError(error => {
        console.log('Error occurred while adding to wishlist:', error);
        return throwError(error); 
      })
    );
  }

  getPulseWishlistProductNames(): string[] {
    const cookies = document.cookie;
    // Find the PulseWishlist cookie
    const match = cookies.match(/(?:^|;\s*)PulseWishlist=([^;]*)/);
    
    if (match && match[1]) {
      // Split the cookie value by commas into an array of product names
      return match[1].split(',');
    }
    return [];
  }

  productNameExistsUserNotLoggedInWishlist(productName: string): boolean{
    return this.getPulseWishlistProductNames().includes(productName);
  }

  public insertUserNotLoggedInWishlistItem(productName: string): Observable<boolean> {
    const products = this.getPulseWishlistProductNames();

    if (!this.productNameExistsUserNotLoggedInWishlist(productName)) {
      products.push(productName);
      document.cookie = `PulseWishlist=${products.join(',')}; path=/;`;
      return of(true);
    }
    return of(false);
  }

  deleteProductFromPulseWishlist(productName: string): void {
    // Get the current wishlist
    const products = this.getPulseWishlistProductNames();
  
    // Filter out the product to be removed
    const updatedProducts = products.filter(product => product !== productName);
  
    // Update the cookie with the remaining products
    if (updatedProducts.length > 0) {
      document.cookie = `PulseWishlist=${updatedProducts.join(',')}; path=/;`;
    } else {
      // If no products remain, clear the cookie
      document.cookie = `PulseWishlist=; path=/;`;
    }
  }

  public getUserNotLoggedInWishlistItems(): Product[] {
    const productNames: string[] = this.getPulseWishlistProductNames();
    const products: Product[] = [];

    productNames.forEach(productName => {
      this.httpClient.get<Product>(this.baseUrl + `/permitted/product/find?productName=${productName}`)
        .subscribe({
          next: (product) => {
            products.push(product)
          }
        })
    });
    return products;
  }

  public wishlistItemExistsUserLoggedin(productName: string): Observable<boolean>{
    const wishlistItemExistsUserLoggedinUrl: string = this.baseUrl + 
              `/wishlist/item/exists?productName=${productName}`;
    return this.httpClient.get<boolean>(wishlistItemExistsUserLoggedinUrl);
  }

  public deleteUserLoggedInWishlistItem(productName: string): Observable<Boolean>{
    const deleteUserLoggedInWishlistItemUrl: string = this.baseUrl + `/wishlist/delete?productName=${productName}`;
    return this.httpClient.delete<Boolean>(deleteUserLoggedInWishlistItemUrl);
  }

  public deleteUserNotLoggedInWishlistItem(productName: string): boolean {
    let done: boolean = false;
    const wishlistCookie = document.cookie
                               .split('; ')
                               .find(row => row.startsWith('pulseWishlist='));
    let wishlistItems = [];  
    
    if (wishlistCookie) {
        try {
            wishlistItems = JSON.parse(decodeURIComponent(wishlistCookie.split('=')[1]));
        } catch (e) {
            console.error('Invalid wishlist cookie:', e);
        }
    }

    const originalLength = wishlistItems.length;
    
    // Filter out the item from the wishlist duke i lan veq itemat qe nuk 
    // jane si productName qe po dojm me fshijm
    wishlistItems = wishlistItems.filter((item: { productName: string; }) =>
       item.productName !== productName);
    
    // Check if any item was removed
    if (wishlistItems.length < originalLength) {
        done = true;
    }
    
    const date = new Date();
    date.setTime(date.getTime() + 7 * 24 * 60 * 60 * 1000);  // 1 week expiration
    document.cookie = `pulseWishlist=${encodeURIComponent(JSON.stringify(wishlistItems))}; 
                       expires=${date.toUTCString()};
                       path=/`;
    return done;
}

  /* --------------------- CartItem requests ----------------------*/

  public findAllCartItemsByUser(): Observable<Product[]>{
    const findAllCartItemsByUserUrl: string = this.baseUrl + "/cart/user"
    return this.httpClient.get<Product[]>(findAllCartItemsByUserUrl);
  }

  public insertCartItem(productName: string): Observable<Boolean>{
    const insertCartItemUrl: string = this.baseUrl + `/cart/insert?productName=${productName}`;
    return this.httpClient.post<Boolean>(insertCartItemUrl, {});
  }

  public deleteCartItem(productName: string): Observable<Boolean>{
    const deleteCartItemUrl: string = this.baseUrl + `/cart/delete?productName=${productName}`;
    return this.httpClient.delete<Boolean>(deleteCartItemUrl);
  }

  public deleteAllUserCartItems(): Observable<Boolean>{
    const deleteAllUserCartItemsUrl: string = this.baseUrl + `/cart/delete/all`;
    return this.httpClient.delete<Boolean>(deleteAllUserCartItemsUrl);
  }

  public cartItemExistsUserLoggedin(productName: string): Observable<boolean>{
    const cartItemExistsUserLoggedinUrl: string = this.baseUrl + 
              `/cart/item/exists?productName=${productName}`;
    return this.httpClient.get<boolean>(cartItemExistsUserLoggedinUrl);
  }
  
  /* --------------------- Payment requests ----------------------*/
  
  public processPayment(paymentDTO: Payment): Observable<string>{
    // const processPaymentUrl: string = this.baseUrl + "/payments/process";
    const processPaymentUrl: string = this.baseUrl + "/permitted/payments/process";
    return this.httpClient.post<string>(processPaymentUrl, paymentDTO);
  }

  public handlePaymentAndPostProcessing(paymentDTO: Payment): Observable<Boolean> {
    // First process the payment, then insert the transaction if successful
    return this.processPayment(paymentDTO).pipe(
      switchMap((paymentResponse: string) => {
        if (paymentResponse.includes("Error")) {
          return new Observable<boolean>((observer) => {
            observer.next(false);
            observer.complete();
          });
        }
        return of(false);
      }
    ));
  }

  public findUserPayments(): Observable<Order[]>{
    const findUserPaymentesUrl: string = this.baseUrl + "/payments/find";
    return this.httpClient.get<Order[]>(findUserPaymentesUrl);
  }

  public findAllPayments(): Observable<FullPayment[]>{
    const findAllPaymentsUrl: string = this.baseUrl + "/admin/payments/find/all";
    return this.httpClient.get<FullPayment[]>(findAllPaymentsUrl);
  }
}

