import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {

  constructor(
    private fb: FormBuilder,
    public dbService: DbService,
    private router: Router
  ) {}

  logout() {
    this.dbService.logout();
    this.router.navigate(['/products']);
    window.location.reload();
  }
}
