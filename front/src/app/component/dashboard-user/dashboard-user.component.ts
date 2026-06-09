import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-user',
  templateUrl: './dashboard-user.component.html',
  styleUrl: './dashboard-user.component.css'
})
export class DashboardUserComponent {
  constructor(private authService: AuthService,private tokenStorageService: TokenStorageService, private router: Router) { }

  ngOnInit(): void {
  }
  logout(): void {
    
    this.tokenStorageService.signOut();
    this.router.navigate(['/login']);
  }
  
  

}
