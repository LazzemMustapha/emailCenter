import { Component } from '@angular/core';
import { ArchivedEmail } from '../../../models/ArchivedEmail ';
import { TokenStorageService } from '../../../services/token-storage.service';
import { ArchivedEmailService } from '../../../services/archived-email.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-archived-email',
  templateUrl: './archived-email.component.html',
  styleUrl: './archived-email.component.css'
})
export class ArchivedEmailComponent {
  archivedEmails: ArchivedEmail[] = [];
  filteredEmails: ArchivedEmail[] = [];
  p: number = 1; // Current page for pagination
 // accountId: number=1;
  selectedEmailId: number | null = null;
  accountId = this.route.snapshot.params['id']

  constructor(
    private archivedEmailService: ArchivedEmailService,
    private tokenService: TokenStorageService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadAccountId();
    this.getAllArchivedEmails();
  }

  loadAccountId(): void {
    const accountId = this.tokenService.getAccountId();
    if (accountId) {
      this.accountId = accountId;
    } else {
      console.error('No account ID found');
    }
  }

  getAllArchivedEmails(): void {
    if (!this.accountId) {
      console.error('Account ID is not defined');
      return;
    }
    this.archivedEmailService.getAllArchivedEmails(this.accountId).subscribe(
      (emails: ArchivedEmail[]) => {
        this.archivedEmails = emails;
        this.filteredEmails = emails;
      },
      error => {
        console.error('Error fetching archived emails:', error);
      }
    );
  }

  filterEmails(): void {
    const sender = (document.getElementById('sender') as HTMLInputElement).value.toLowerCase();
    const startDate = (document.getElementById('startDate') as HTMLInputElement).value;
    const endDate = (document.getElementById('endDate') as HTMLInputElement).value;

    this.filteredEmails = this.archivedEmails.filter(email => {
      const emailSender = email.sender.toLowerCase();
      const emailDate = email.date;

      let showEmail = true;

      if (sender && !emailSender.includes(sender)) {
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

  unarchiveEmail(email: ArchivedEmail): void {
    console.log('Unarchive email:', email);
    // Implement unarchive functionality
  }

  deleteEmail(email: ArchivedEmail): void {
    if (this.accountId && email.id) {
      this.archivedEmailService.deleteArchivedEmail(this.accountId, email.id).subscribe(
        response => {
          console.log('Email deleted successfully:', response);
          this.getAllArchivedEmails();
          
        },
        error => {
          console.error('Error deleting email:', error);
        }
      );
    }
  }

  refreshEmailList(): void {
    this.getAllArchivedEmails();
  }

}
