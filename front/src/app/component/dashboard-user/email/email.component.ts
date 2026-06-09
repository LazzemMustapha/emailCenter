import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as bootstrap from 'bootstrap';
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

  constructor(private route: ActivatedRoute, private emailService: EmailService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.emailId = params['id']; // Récupérer l'ID de l'email depuis l'URL
      this.getEmailDetails(this.emailId); // Appeler la méthode pour récupérer les détails de l'email
    });
  }

  // Méthode pour récupérer les détails de l'e-mail par son ID
  getEmailDetails(emailId: number): void {
    this.emailService.getEmailById(emailId).subscribe(
      (response: Email) => {
        this.email = response; // Assigner la réponse à la variable email
      },
      (error) => {
        console.log('Error fetching email details:', error);
      }
    );
  }


}
