import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { EmailService } from '../../../services/email.service';
import { Email } from '../../../models/email';

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  emailId!: number;
  email: Email | undefined;
  safeBody: SafeHtml | undefined;

  constructor(
    private route: ActivatedRoute,
    private emailService: EmailService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.emailId = params['id'];
      this.getEmailDetails(this.emailId);
    });
  }

  getEmailDetails(emailId: number): void {
    this.emailService.getEmailById(emailId).subscribe(
      (response: Email) => {
        this.email = response;
        this.safeBody = this.sanitizer.bypassSecurityTrustHtml(response.body);
      },
      (error) => {
        console.log('Error fetching email details:', error);
      }
    );
  }
}