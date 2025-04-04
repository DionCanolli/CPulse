import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDashboardPaymentsComponent } from './admin-dashboard-payments.component';

describe('AdminDashboardPaymentsComponent', () => {
  let component: AdminDashboardPaymentsComponent;
  let fixture: ComponentFixture<AdminDashboardPaymentsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminDashboardPaymentsComponent]
    });
    fixture = TestBed.createComponent(AdminDashboardPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
