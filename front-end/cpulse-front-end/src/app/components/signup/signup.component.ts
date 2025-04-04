import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/entities/user';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {

  userForm: FormGroup;

  // constructor(
  //   private router: Router,
  //   private fb: FormBuilder,
  //   public dbService: DbService
  // ) {
  //   this.userForm = this.fb.group({
  //     firstName: ['', [Validators.required, Validators.minLength(3)]],
  //     lastName: ['', [Validators.required, Validators.minLength(3)]],
  //     username: ['', [Validators.required, Validators.minLength(5)]],
  //     email: ['', [Validators.required, Validators.email]],
  //     phoneNumber: ['', [Validators.required, Validators.pattern(/^\+\d{7,}$/)]],
  //     password: ['',
  //       [
  //         Validators.required,
  //         Validators.minLength(6),
  //         Validators.pattern(/^(?=.*[A-Z])(?=.*\d).{6,}$/), // min 1 capital letter edhe min 1 number, edhe 6+ karaktere
  //       ],
  //     ]
  //   });
  // }

  constructor(
    private router: Router,
    private fb: FormBuilder,
    public dbService: DbService
  ) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      username: ['', [Validators.required, Validators.minLength(5)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\+?\d{7,}$/)]], // Accepts with or without "+"
      password: ['',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{6,}$/), // Must include a capital letter, number, and symbol
        ],
      ]
    });
  }

  onSubmit() {
    if (this.userForm.valid ) {
      const newUser: User = {
        ...this.userForm.value,
        roleId: 1 
      };

      this.dbService.signUp(newUser).subscribe({
        next: (response) => {
          alert('User created successfully!');
          this.router.navigate(['/login']); // Kerkesa 1
        },
        error: () => {
          alert('Failed to create user. Please try again.');
        }
      });
    } else {
      alert('Form is invalid. Please check the fields.');
    }
  }
}
