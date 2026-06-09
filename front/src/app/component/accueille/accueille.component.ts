import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { Account } from '../../models/account';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { TokenStorageService } from '../../services/token-storage.service';
import { EmailService } from '../../services/email.service';

@Component({
  selector: 'app-accueille',
  templateUrl: './accueille.component.html',
  styleUrls: ['./accueille.component.css']
})
export class AccueilleComponent implements OnInit {
 

  gmailForm: FormGroup;
  

  constructor(
    private fb: FormBuilder,
    private accountService: AccountService,
    private router: Router,
    private authService: AuthService,
    private userservice: UserService,
    private tokens: TokenStorageService,
    private emailService: EmailService
  ) {
    this.gmailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      serveur: ['imap.gmail.com', Validators.required],
      port: [993, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    console.log('Form submitted');
    if (this.gmailForm.valid) {
      console.log('Form is valid');
      const account: Account = this.gmailForm.value;
      const userId = this.tokens.getUserId(); 
      console.log(userId);

      this.accountService.createAccount(userId, account).subscribe(
        response => {
          console.log('Account created successfully:', response);
          if (response.id) {
            this.emailService.fetchAndSaveEmails(response.id).subscribe(
              result => {
                console.log('Emails fetched and saved successfully:', result);
                this.router.navigate(['/user/account']);
              },
              error => {
                console.error('Error fetching and saving emails:', error);
                console.error('Error details:', error.error);
                this.router.navigate(['/user/account']);
                
              }
            );
          } else {
            console.error('Account ID is missing in the response:', response);
          }
        },
        error => {
          console.error('Error creating account:', error);
        }
      );
    } else {
      console.log('Form is invalid');
    }
  }
  
}

