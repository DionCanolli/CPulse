import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { format } from 'date-fns/format';
import { FullPayment } from 'src/app/entities/full-payment';
import { DbService } from 'src/app/services/db.service';

@Component({
  selector: 'app-admin-dashboard-payments',
  templateUrl: './admin-dashboard-payments.component.html',
  styleUrls: ['./admin-dashboard-payments.component.css']
})
export class AdminDashboardPaymentsComponent {

  payments: FullPayment[] = [];

  constructor(
    public dbService: DbService,
    public activatedRoute: ActivatedRoute,
    public router: Router
  ){}

  ngOnInit(){
    this.dbService.findAllPayments().subscribe({
      next: payments => {
        this.payments = payments.map(payment => new FullPayment(payment.email, payment.amount, new Date(payment.paymentDate!)));
      }
    })
  }

  formatDate(date: Date): string {
    return format(date, 'yyyy-MM-dd HH:mm:ss');
  }
}
