import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/userr.service';
import { USER } from '../../../services/regres';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html'
})
export class AddUserComponent {
  addUserForm: FormGroup;

  constructor(private fb: FormBuilder, private userService: UserService, private toastr: ToastrService,private router: Router)  {
    this.addUserForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      username: ['', Validators.required],
      role: ['user', Validators.required]
    });
  }

  onSubmit() {
    if (this.addUserForm.valid) {
      const newUser: USER = this.addUserForm.value;

      // Vérifie si l'utilisateur existe déjà
      this.userService.findByEmail(newUser.email).subscribe(
        existingUser => {
          if (existingUser) {
            this.toastr.error('User already exists', 'Error');
          } else {
            // L'utilisateur n'existe pas, ajout de l'utilisateur
            this.userService.addUser(newUser).subscribe(
              response => {
                this.toastr.success('User added successfully', 'Success');
                // Réinitialisez le formulaire ou naviguez vers une autre page
                this.router.navigateByUrl('/admin/listuser');
              },
              error => {
                this.toastr.error('Error adding user', 'Error');
              }
            );
          }
        },
        error => {
          this.toastr.error('Error checking existing user', 'Error');
        }
      );
    }
  }
}
