import { Component, OnInit } from '@angular/core';

import {Router} from "@angular/router";
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-regitrec',
  templateUrl: './register1.component.html',
  styleUrls: ['./register1.component.css']
})
export class Register1Component implements OnInit {
  form: any = {};
  isSuccessful = false;

  // isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  constructor(private router: Router,private authService: AuthService) { }
  ngOnInit() {
  }
  onSubmit() {

    this.authService.register1(this.form).subscribe(
      data => {
        if (data.error==true){this.isSignUpFailed = true;}
        else{
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.router.navigate(['login']);
      }
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }
}
