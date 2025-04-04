import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FullPayment } from 'src/app/entities/full-payment';
import { User } from 'src/app/entities/user';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-admin-dashboard-users',
  templateUrl: './admin-dashboard-users.component.html',
  styleUrls: ['./admin-dashboard-users.component.css']
})
export class AdminDashboardUsersComponent {
  
  users: User[] = [];

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  ngOnInit(){
    this.dbService.findAllUsers().subscribe({
      next: users => {
        this.users = users;
      }
    })
  }
}
