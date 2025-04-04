import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from 'src/app/entities/user';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {
  
  user: User | null = null;
  userForm: FormGroup;
  
  constructor(
    private fb: FormBuilder,
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      username: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', []],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\+\d{7,}$/)]],
      password: ['', []],
      roleId: ['', []]
    });
  }

  ngOnInit(){
    this.getUser().subscribe({
      next: user => {
        this.user = user;
        this.userForm.patchValue({
          firstName: user.firstName,
          lastName: user.lastName,
          username: user.username,
          email: user.email,
          phoneNumber: user.phoneNumber,
          password: user.password,
          roleId: user.roleId,
        });
      }
    })
  }

  getUser(): Observable<User>{
    return this.dbService.findUser();
  }
  
  viewProducts() {
    this.router.navigate(['/products']);
  }

  viewCart() {
    this.router.navigate(['/cart']);
  }

  viewOrders() {
    this.router.navigate(['/orders']);
  }

  viewWishlist() {
    this.router.navigate(['/wishlist']);
  }

  update() {
    if (this.userForm.valid) {
      const newUser: User = {
        ...this.userForm.value,
      };

      this.dbService.updateUser(newUser).subscribe({
        next: (response) => {
          alert('User updated successfully!');
          window.location.reload()
        },
        error: () => {
          alert('Failed to update user. Please try again.');
        }
      });
    } else {
      alert('Form is invalid. Please check the fields.');
    }
  }

  deleteAccount(){
    this.dbService.deleteUser().subscribe({
      next: response => {
        if(response){
          this.dbService.logout();
          this.router.navigate(['/products']);
          window.location.reload();
        }else{
          console.log("Something went wrong!");
        }
      },
      error: err => {
        console.log("Something went wrong!");
      }
    })

  }
}
