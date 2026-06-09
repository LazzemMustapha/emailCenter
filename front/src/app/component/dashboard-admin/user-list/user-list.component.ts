import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserService } from '../../../services/userr.service';
import { USER } from '../../../services/regres';


@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: USER[] = [];
  searchQuery: string = '';
  editUserForm: FormGroup;
  selectedUser: USER | null = null;
  userInfo: USER | null = null; 


  constructor(
    private userService: UserService,
    private fb: FormBuilder
  
  ) {
    this.editUserForm = this.fb.group({
      id: [''],
      username: [''],
      email: [''],
      password: [''],
      role: ['']
    });
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe((data: USER[]) => {
      this.users = data;
     
    });
  }

  filterUsers(): USER[] {
    if (!this.searchQuery) {
      return this.users;
    }
    return this.users.filter(user => 
      (user.username && user.username.includes(this.searchQuery)) || 
      (user.email && user.email.includes(this.searchQuery))
    );
  }

  deleteUser(id: number): void {
    console.log(`Attempting to delete user with id: ${id}`);
    this.userService.removeUser(id).subscribe(
      () => {
        console.log(`User with id: ${id} deleted successfully`);
        this.loadUsers();
      
       
      },
      (error) => {
        console.error(`Error deleting user with id: ${id}`, error);
      }
    );
  }
  selectUser(user: USER): void {
    this.selectedUser = user;
    this.editUserForm.patchValue(user);
  }

  updateUser(): void {
    
    if (this.editUserForm.valid) {
      const updatedUser = this.editUserForm.value;
      this.userService.updateUser(updatedUser.id, updatedUser).subscribe(() => {
        this.loadUsers();
       
        const closeModalBtn = document.querySelector('#editUserModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
      });
    }
  }
  getUserInfo(id: number): void {
    this.userService.getUserInfo(id).subscribe((userInfo: USER) => {
      this.userInfo = userInfo;
      // Afficher les informations dans le modal
      const modal = document.getElementById('userInfoModal');
      if (modal) {
        modal.classList.add('show'); // Affiche le modal
        modal.setAttribute('aria-modal', 'true'); // Définir l'attribut aria-modal sur true
        modal.setAttribute('style', 'display: block'); // Afficher le modal en utilisant le style display
      }
    });
  }
  
  
}
