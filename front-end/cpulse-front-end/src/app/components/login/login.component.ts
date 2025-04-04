import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/entities/user';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  userForm: FormGroup;
  isAdmin: number = 0;
  isUser: number = 0;

  constructor(
    private fb: FormBuilder,
    public dbService: DbService,
    private router: Router
  ) {
    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.userForm.valid ) {
      const user: User = {
        email: this.userForm.value.email,
        password: this.userForm.value.password
      };

      this.dbService.login(user).subscribe({
        next: (response) => {
          document.cookie = `PulseWishlist=; path=/;`;
          this.dbService.verifyAdminLoggedIn().subscribe({
            next: result => {
              this.isAdmin = result;
              if(this.isAdmin === 1){
                this.router.navigate(['/admin-dashboard']);
              }
            }
          });
          this.dbService.verifyUserLoggedIn().subscribe({
            next: result => {
              this.isUser = result;
              if(this.isUser === 1){
                this.router.navigate(['/products']);
              }
            }
          });
        },
        error: () => {
          alert('Login failed!');
        }
      });
    } else {
      alert('Form is invalid. Please check the fields.');
    }
  }
}
