import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../services/token-storage.service';





@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent  {
  constructor(private authService: AuthService,private tokenStorageService: TokenStorageService, private router: Router) { }

  ngOnInit(): void {
  }
  logout(): void {
    
    this.tokenStorageService.signOut();
    this.router.navigate(['/login']);
  }
  

 
  }

