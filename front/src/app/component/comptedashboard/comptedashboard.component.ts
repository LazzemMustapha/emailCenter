import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-comptedashboard',
  templateUrl: './comptedashboard.component.html',
  styleUrl: './comptedashboard.component.css'
})
export class ComptedashboardComponent {
  constructor(private authService: AuthService,private route : ActivatedRoute,private tokenStorageService: TokenStorageService, private router: Router) { }
id=this.route.snapshot.params['id']
  ngOnInit(): void {

  }
  navigateToArchivingRules(): void {
    this.router.navigate(['/dash/:id/archivingRulee', this.id]);
  }
  navigateTolistemeail(): void {
    this.router.navigate(['/dash/:id/listeemail' , this.id]);
  }
  navigateToarchiv(): void {
    this.router.navigate(['/dash/:id/emailarchivée' , this.id]);
  }
  logout(): void {
    
    this.tokenStorageService.signOut();
    this.router.navigate(['/login']);
  }
  
  

}
