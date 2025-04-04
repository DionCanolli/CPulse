import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Payment } from 'src/app/entities/payment';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent {
  products: Product[] = [];
  username: string | null = null;
  totalPrice: number = 0;
  paymentForm: FormGroup;
  isSpecialOrder: boolean = false;
  successfullyDecreasedQuantity: boolean = true;
  isUserAuthenticated: number = 0;
  isAdminAuthenticated: number = 0; 
  countries: string[] = ['USA', 'Canada', 'UK', 'Germany', 'France', 'Italy', 'Australia', 'India', 'Japan', 'Kosovo', 'Albania'];

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    private fb: FormBuilder
  ){
    this.paymentForm = this.fb.group({
        cardNumber: ['', [Validators.required, Validators.minLength(16)]],
        cardHolderName: ['', [Validators.required, Validators.minLength(5)]],
        expiryMonth: ['', [Validators.required]],
        expiryYear: ['', [Validators.required]],
        cvv: ['', [Validators.required, Validators.minLength(3)]],
        billingAddress: ['', [Validators.required, Validators.minLength(10)]],
        zipCode: ['', [Validators.required, Validators.minLength(5)]],
        country: ['', [Validators.required]],
        amount: [this.totalPrice, []],
        userEmail:['', [Validators.required, Validators.email]]
      });
  }

  ngOnInit(){
    this.products = history.state.products;
    this.isSpecialOrder = history.state.isSpecialOrder;
    this.findTotalPrice().subscribe({
      next: totalPrice => {
        this.totalPrice = totalPrice;
      }
    });
    
    this.verifyUserAuthenticated().subscribe({
      next: response => {
        this.isUserAuthenticated = response;
        this.formGroupInitializer();
      }
    });

    this.verifyAdminAuthenticated().subscribe({
      next: response => {
        this.isAdminAuthenticated = response;
        this.formGroupInitializer();
      }
    });       
  }

  formGroupInitializer() {
    if(this.isUserAuthenticated == 1 || this.isAdminAuthenticated == 1){
      this.paymentForm = this.fb.group({
        cardNumber: ['', [Validators.required, Validators.minLength(16), Validators.maxLength(16)]],
        cardHolderName: ['', [Validators.required, Validators.minLength(5)]],
        expiryMonth: ['', [Validators.required]],
        expiryYear: ['', [Validators.required]],
        cvv: ['', [Validators.required, Validators.minLength(3)]],
        billingAddress: ['', [Validators.required, Validators.minLength(10)]],
        zipCode: ['', [Validators.required, Validators.minLength(5)]],
        country: ['', [Validators.required]],
        amount: [this.totalPrice, []]
      });
    }else {
      this.paymentForm = this.fb.group({
        cardNumber: ['', [Validators.required, Validators.minLength(16), Validators.maxLength(16)]],
        cardHolderName: ['', [Validators.required, Validators.minLength(5)]],
        expiryMonth: ['', [Validators.required]],
        expiryYear: ['', [Validators.required]],
        cvv: ['', [Validators.required, Validators.minLength(3)]],
        billingAddress: ['', [Validators.required, Validators.minLength(10)]],
        zipCode: ['', [Validators.required, Validators.minLength(5)]],
        country: ['', [Validators.required]],
        amount: [this.totalPrice, []],
        userEmail:['', [Validators.required, Validators.email]]
      });
    }
  }

  verifyUserAuthenticated(): Observable<number> {
    return this.dbService.verifyUserLoggedIn();
  }

  verifyAdminAuthenticated(): Observable<number> {
    return this.dbService.verifyAdminLoggedIn();
  }

  findTotalPrice(): Observable<number>{
    let total: number = 0;
    for(let product of this.products){
      total += product.productPrice!;
    } 
    return of(total);
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

  viewCart() {
    this.router.navigate(['/cart']);
  }

  logout() {
    this.dbService.logout();
    this.router.navigate(['/login']);
  }

  // fillimisht pregadited objekti payment per me qu ne backend me te dhenat e karteles dhe cmimin perfundimtar,
  // pastaj ulet stocku i secilit product qe po don me ble per 1, nese stocku eshte > 0, dhe nese nuk shkon
  // dicka ne rregull, successfullyDecreasedQuantity bohet false, e nese eshte false ather smun me 
  // ble asni produkt, e nee eshte true i bie qe e ule stocun per 1, ather mun mi ble produktet/in
  onSubmit() {
    if(this.isAdminAuthenticated == 1 || this.isUserAuthenticated == 1){
      if (this.paymentForm.valid ) {
        const payment: Payment = {
          cardNumber: this.paymentForm.value.cardNumber,
          expiryMonth: this.paymentForm.value.expiryMonth,
          expiryYear: this.paymentForm.value.expiryYear,
          cvv: this.paymentForm.value.cvv,
          amount: this.totalPrice
        };
  
        try{
          for(let product of this.products){
            if(product.productStockQuantity == 0){
              this.successfullyDecreasedQuantity = false;
              return;
            }
            this.dbService.modifyProduct(product.productName!, product.productStockQuantity!-1).subscribe({
              next: response => {
              },
              error: err => {
                this.successfullyDecreasedQuantity = false;
              }
            })
          }
  
          if(this.successfullyDecreasedQuantity){
            this.dbService.processPayment(payment).subscribe({
              next: (response) => {
                console.log(payment);
                alert('Success!');
                if(!this.isSpecialOrder){
                  this.dbService.deleteAllUserCartItems().subscribe({
                    next: response => {
                      console.log("Success!");
                    }
                  })  
                }
                this.router.navigate(['/products']);
              },
              error: () => {
                console.log(payment);
                alert('Failed to pay!');
              }
            });
          }
        }catch(error){
          alert('Failed to pay!');
        }
      } else {
        alert('Form is invalid. Please check the fields.');
      }
    }else{
      var currentYear: number = new Date().getFullYear() % 100;
      var currentMonth: number = new Date().getMonth() + 1;
      const cardMonth: string = this.paymentForm.value.expiryMonth; // "06"
      const cardYear: string = this.paymentForm.value.expiryYear; // "25"

      // Convert values to numbers
      const cardMonthNumber: number = parseInt(cardMonth, 10);
      const cardYearNumber: number = parseInt(cardYear, 10);

      console.log(currentMonth + " " + cardMonth + " " + currentYear + " " + cardYear);

      if(cardYearNumber > currentYear || (cardYearNumber === currentYear && cardMonthNumber >= currentMonth)) {
        try{
          for(let product of this.products){
            if(product.productStockQuantity == 0){
              this.successfullyDecreasedQuantity = false;
              alert('3!');
              break;
            }
            this.dbService.modifyProduct(product.productName!, product.productStockQuantity!-1).subscribe({
              next: response => {
              },
              error: err => {
                this.successfullyDecreasedQuantity = false;
              }
            })
          }
        }catch(error){
          console.log(error);
        }
        
        alert('Success!');
        this.router.navigate(['/products']);
      }else{
        alert('Failed to pay!');
      }      
    }
  }
}
