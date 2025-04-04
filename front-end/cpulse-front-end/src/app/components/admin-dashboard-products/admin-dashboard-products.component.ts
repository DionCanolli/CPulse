import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from 'src/app/entities/product';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-admin-dashboard-products',
  templateUrl: './admin-dashboard-products.component.html',
  styleUrls: ['./admin-dashboard-products.component.css']
})
export class AdminDashboardProductsComponent {

  public products: Product[] = [];
  productForm: FormGroup;

  constructor(
      public dbService: DbService,
      public activatedRoute: ActivatedRoute,
      public router: Router,
      public fb: FormBuilder
    ){
      this.productForm = this.fb.group({
        productName: ['', Validators.required],
        productDescription: ['', Validators.required],
        productCategory: ['', Validators.required],
        productPrice: [null, [Validators.required, Validators.min(0.01)]], 
        productStockQuantity: [1, [Validators.required, Validators.min(1)]], 
        productImage: [null, Validators.required]
      });
    }
  
    ngOnInit(){
      this.dbService.findAllProductsNotPaginated().subscribe({
        next: products => {
          this.products = products;
        }
      })
    }

    addProduct(): void {

      var productImageEdited: string = this.productForm.value.productName.replace(/ /g, "_");
      

      const productDTO = {
        productName: this.productForm.value.productName,
        productDescription: this.productForm.value.productDescription,
        productCategory: this.productForm.value.productCategory,
        productPrice: this.productForm.value.productPrice,
        productStockQuantity: this.productForm.value.productStockQuantity,
        productImageUrl: `C:\\Users\\HP\\Desktop\\AAB\\Viti 3\\Semestri 1\\Projket\\Kodi\\front-end\\dtpulse-front-end\\src\\assets\\images\\${productImageEdited}.jpg`,
      };
    
      const productImage: File = this.productForm.get('productImage')?.value;
    
      this.dbService.insertProduct(productDTO, productImage).subscribe({
        next: (response) => {
          alert("Success!");
          window.location.reload();
        },
        error: (err) => {
          alert("Something went wrong!");
        },
      });
    }
    
    onFileSelected(event: any): void {
      const file = event.target.files[0];
      if (file) {
        this.productForm.patchValue({ productImage: file });
        this.productForm.get('productImage')?.updateValueAndValidity();
      }
    }

    modifyProduct(productName: string, quantity: number) {
      if(productName == null || productName.length == 0){
        return;
      }
      if(quantity == null){
        quantity = 0;
      }
      this.dbService.modifyProduct(productName, quantity).subscribe({
        next: response => {
          if (response) {
            alert("Success!");
            window.location.reload();
          }else{
            alert("Something went wrong!");
          }
        }
      })
    }

    deleteProduct(productName: string){
      this.dbService.deleteProduct(productName).subscribe({
        next: response => {
          if (response) {
            alert("Success!");
            window.location.reload();
          }else{
            alert("Something went wrong!");
          }
        }
      })
    }
}
