import { Component, OnInit, AfterViewInit } from '@angular/core';
import { EmailService } from '../../../services/email.service';
import { Email } from '../../../models/email';
import { DomainEntityService } from '../../../services/domainentity.service';
import { DomainEntity } from '../../../models/domain-entity';
import { ActivatedRoute, Router } from '@angular/router';

declare var bootstrap: any;

@Component({
  selector: 'app-list-email',
  templateUrl: './list-email.component.html',
  styleUrls: ['./list-email.component.css']
})
export class ListEmailComponent implements OnInit {
  emails: Email[] = [];
  filteredEmails: Email[] = [];
  p: number = 1; // Current page for pagination
  //accountId: number = 1; // Replace with actual account ID
  selectedEmailId: number | null = null; // Store the selected email ID
  emailDomains: DomainEntity[] = []; // List of email domains
  selectedDomain: string = ''; // Selected email domain
   accountId = this.route.snapshot.params['id'];

  constructor(private emailService: EmailService,private route: ActivatedRoute, private domainService: DomainEntityService,private router: Router) { }
 
  ngOnInit(): void {
    this.getAllEmails();
    //this.getAllDomains();
    this.getAllDomainsByAccountId(this.accountId);
  }


  getAllEmails(): void {
    this.emailService.getAllEmailsByAccountId(this.accountId).subscribe(
      (emails: Email[]) => {
        this.emails = emails;
        this.filteredEmails = emails;
      },
      error => {
        console.error('Error fetching emails:', error);
      }
    );
  }
  getAllDomainsByAccountId(accountId: number): void {
    this.domainService.getAllDomainEntitiesByAccountId(this.accountId).subscribe(
      (domains: DomainEntity[]) => {
        this.emailDomains = domains;
      },
      error => {
        console.error('Error fetching domain entities:', error);
      }
    );
  }
  /*getAllDomains(): void {
    this.domainService.getAllDomainEntities().subscribe(
      (domains: DomainEntity[]) => {
        this.emailDomains = domains;
      },
      error => {
        console.error('Error fetching domains:', error);
      }
    );
  }*/

  
  filterByEmailDomain(): void {
    if (this.selectedDomain) {
      this.emailService.searchByDomain(this.selectedDomain, this.accountId).subscribe(
        (emails: Email[]) => {
          this.filteredEmails = emails;
        },
        error => {
          console.error('Error fetching emails for domain:', error);
        }
      );
    } else {
      this.filteredEmails = this.emails;
    }
  }

  filterEmails(): void {
    const sender = (document.getElementById('sender') as HTMLInputElement).value.toLowerCase();
    const subject = (document.getElementById('subject') as HTMLInputElement).value.toLowerCase();
    const startDate = (document.getElementById('startDate') as HTMLInputElement).value;
    const endDate = (document.getElementById('endDate') as HTMLInputElement).value;

    this.filteredEmails = this.emails.filter(email => {
      const emailSender = email.sender.toLowerCase();
      const emailSubject = email.subject.toLowerCase();
      const emailDate = email.date;

      let showEmail = true;

      if (sender && !emailSender.includes(sender)) {
        showEmail = false;
      }

      if (subject && !emailSubject.includes(subject)) {
        showEmail = false;
      }

      if (startDate && emailDate < startDate) {
        showEmail = false;
      }

      if (endDate && emailDate > endDate) {
        showEmail = false;
      }

      return showEmail;
    });
  }

  selectAllEmails(event: any): void {
    const checkboxes = document.querySelectorAll('.checkbox');
    checkboxes.forEach((checkbox: any) => {
      checkbox.checked = event.target.checked;
    });
  }

  archiveEmail(emailId: number | null): void {
    if (emailId !== null) {
      this.emailService.archiveEmail(emailId, this.accountId).subscribe(
        response => {
          console.log('Email archived successfully:', response);
          this.getAllEmails();
        },
        error => {
          console.error('Error archiving email:', error);
        }
      );
    } else {
      console.error('No email selected for archiving.');
    }
  }

  deleteEmail(emailId: number | null): void {
    if (emailId !== null) {
      this.emailService.deleteEmail(emailId, this.accountId).subscribe(
        response => {
          console.log('Email deleted successfully:', response);
          this.getAllEmails();
        }
        
        
      );
      
    } else {
      console.error('No email selected for deletion.');
    }
  }
  refreshEmailList(): void {
    this.getAllEmails();
    //this.getAllDomains();
    //this.getAllDomainsByAccountId();
    
  }
  goToEmailDetails(emailId: number): void {
    // Rediriger vers la page de détails de l'email avec l'ID de l'email
    this.router.navigate(['/user/email', emailId]);
  }
  
}

