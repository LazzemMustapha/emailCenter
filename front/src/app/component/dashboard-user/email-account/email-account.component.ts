import { Component, OnInit } from '@angular/core';
import { Account } from '../../../models/account';
import { AccountService } from '../../../services/account.service';
import { Router } from '@angular/router';
import { TokenStorageService } from '../../../services/token-storage.service';
import { EmailService } from '../../../services/email.service';

@Component({
  selector: 'app-email-account',
  templateUrl: './email-account.component.html',
  styleUrls: ['./email-account.component.css']
})
export class EmailAccountComponent implements OnInit {
  /*emailAccounts: Account[] = [];
  newEmailAccount: Account = new Account();
  editEmailAccount: Account = new Account();
  userId: number = 1; // Assuming a static userId for demonstration

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.getAccountsByUserId();
  }

  getAccountsByUserId(): void {
    this.accountService.getAccountsByUserId(this.userId).subscribe(
      (accounts) => this.emailAccounts = accounts,
      (error) => console.error('Error fetching accounts', error)
    );
  }

  addEmailAccount(): void {
    this.accountService.createAccount(this.userId, this.newEmailAccount).subscribe(
      (account) => {
        this.emailAccounts.push(account);
        this.newEmailAccount = new Account(); // Reset the form
        $('#addEmailAccountModal').modal('hide'); // Close the modal
      },
      (error) => console.error('Error adding account', error)
    );
  }

  updateEmailAccount(): void {
    this.accountService.updateAccount(this.userId, this.editEmailAccount.id, this.editEmailAccount).subscribe(
      (updatedAccount) => {
        const index = this.emailAccounts.findIndex(acc => acc.id === updatedAccount.id);
        if (index !== -1) {
          this.emailAccounts[index] = updatedAccount;
        }
        this.editEmailAccount = new Account(); // Reset the form
        $('#editEmailAccountModal').modal('hide'); // Close the modal
      },
      (error) => {
        console.error('Error updating account', error);
        console.error('Error details:', error.message);
        console.error('Backend returned code', error.status, 'body was:', error.error);
      }
    );
  }

  deleteEmailAccount(account: Account): void {
    if (confirm('Are you sure you want to delete this account?')) {
      this.accountService.deleteAccount(this.userId, account.id).subscribe(
        () => this.emailAccounts = this.emailAccounts.filter(acc => acc.id !== account.id),
        (error) => console.error('Error deleting account', error)
      );
    }
  }

  navigateToArchivingRules(account: Account): void {
    this.router.navigate(['/user/archivingRule', account.id]);
  }
  selectEmailAccount(account: Account): void {
    this.editEmailAccount = { ...account };
  }*/
  accounts: Account[] = []; // Array pour stocker les comptes
  userId!: number ; // Exemple de userId, vous pouvez le modifier selon vos besoins
  editEmailAccount: Account | null = null; // Variable pour stocker le compte actuellement en cours de modification
  newEmailAccount: Account = new Account(); // Nouvel objet de compte pour l'ajout
  

  // Injectez le service AccountService dans le composant
  constructor(private route:Router,private accountService: AccountService,private router: Router , private tokens: TokenStorageService, private emailService: EmailService) { }

  // Méthode ngOnInit qui est appelée lors de l'initialisation du composant
  ngOnInit(): void {
    this.getAccountsByUserId(); // Chargez les comptes lors de l'initialisation
  }

  // Méthode pour récupérer les comptes par utilisateur
  getAccountsByUserId(): void {
    this.userId=this.tokens.getUserId() ;
    this.accountService.getAccountsByUserId(this.userId).subscribe(
      (data: Account[]) => {
        this.accounts = data;
        console.log(this.accounts) // Attribuez les comptes récupérés à la variable accounts
      },
      (error: any) => {
        console.error('Error fetching accounts', error); // Gérez les erreurs s'il y en a
      }
    );
  }

  // Méthode pour ajouter un nouveau compte
  addAccount(): void {
    this.accountService.createAccount(this.userId, this.newEmailAccount).subscribe(
      (data: Account) => {
        this.accounts.push(data);
        this.newEmailAccount = new Account(); // Réinitialisez le nouvel objet de compte pour l'ajout ultérieur

        // Appel de la méthode fetchAndSaveEmails après la création du compte
        this.emailService.fetchAndSaveEmails(data.id).subscribe(
          (response) => {
            console.log('Emails fetched and saved successfully:', response);
          },
          (error) => {
            console.error('Error fetching and saving emails:', error);
          }
        );
      },
      (error: any) => {
        console.error('Error adding account', error);
      }
    );
    const closeModalBtn = document.querySelector('#addEmailAccountModal .btn-close') as HTMLElement;
    if (closeModalBtn) closeModalBtn.click();
  }

  // Méthode pour mettre à jour un compte existant
  editAccount(): void {
    if (this.editEmailAccount) {
      this.accountService.updateAccount(this.userId, this.editEmailAccount.id, this.editEmailAccount).subscribe(
        () => {
          this.getAccountsByUserId(); // Rechargez la liste des comptes après la mise à jour
        },
        (error: any) => {
          console.error('Error updating account', error); // Gérez les erreurs s'il y en a
        }
      );
      const closeModalBtn = document.querySelector('#editEmailAccountModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
    }
  }

  // Méthode pour supprimer un compte existant
  deleteAccount(accountId: number): void {
    this.accountService.deleteAccount(this.userId, accountId).subscribe(
      () => {
        this.accounts = this.accounts.filter(account => account.id !== accountId); // Filtrer le compte supprimé de la liste des comptes
        
      },
      (error: any) => {
        console.error('Error deleting account', error); // Gérez les erreurs s'il y en a
      
      }
    );
    window.location.reload();
  }

  // Méthode pour sélectionner un compte à modifier
  selectEmailAccount(account: Account): void {
    this.editEmailAccount = { ...account }; // Copiez l'objet de compte sélectionné dans la variable d'édition
  }
  navigateToArchivingRules(id:number): void {
    this.router.navigate(['/user/archivingRule', id]);
  }
  navigateTolistemeail(id:number): void {
    this.router.navigate(['/user/listemail' , id]);
  }
  navigatetodash(id:number): void {
    this.router.navigate(['/dash' , id]);
  }
}