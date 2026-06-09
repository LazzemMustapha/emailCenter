import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TokenStorageService } from '../../services/token-storage.service';
import {Router} from "@angular/router";
import { UserService } from '../../services/userr.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  role: string[] = [];

  constructor(
    private route: Router,
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private userService: UserService
  ) { }

  ngOnInit() {
    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      const userRole = this.tokenStorage.getUser().role;
      if (userRole) {
        this.role = JSON.parse(userRole);
      }
    }
  }

 
  onSubmit() {
    this.authService.userLogin(this.form).subscribe(
      (data: any) => {
        this.tokenStorage.saveToken(data.access_token);
        this.tokenStorage.saveUser(data.user);
        this.isLoginFailed = false;
        this.isLoggedIn = true;

        const userId = data.user.id;

        // Vérifier si le rôle "ADMIN" est présent dans la liste des rôles de l'utilisateur
        const roles = data.user.authorities.map((authority: any) => authority.authority);
        if (roles.includes("admin:create") || roles.includes("admin:update")) {
          this.route.navigate(['admin']);
        } else {
          if (this.tokenStorage.isFirstLogin(userId)) {
            this.tokenStorage.setFirstLogin(userId);
            this.route.navigate(['accueille']);
          } else {
            this.route.navigate(['/user/account']);
          }
        }
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }
  resetPassword(): void {
    const email = (document.getElementById('forgotEmail') as HTMLInputElement).value; // Récupérez la valeur de l'email depuis l'élément HTML
    this.userService.resetPassword({ email: email }).subscribe(
      response => {
        console.log('Reset password request sent successfully:', response);
        // Ajoutez ici toute logique supplémentaire nécessaire en cas de succès
      },
      error => {
        console.error('Error sending reset password request:', error);
        // Ajoutez ici toute logique de gestion des erreurs supplémentaire nécessaire en cas d'échec
      }
    );
    const closeModalBtn = document.querySelector('#forgotPasswordModal .btn-close') as HTMLElement;
    if (closeModalBtn) closeModalBtn.click();
  }
  
  reloadPage() {
    window.location.reload();
  }
}
