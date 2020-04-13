import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from '../authentication.service';
import { AlertService } from '../alert.service';

import { User } from '../models/user';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  error: string;

  constructor(
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  register(login: string, firstname: string, lastname: string,
            email: string, password: string, confirmPassword: string){

    if (password !== confirmPassword) {
      this.alertService.error("Passwords don't match!");
      return;
    }

    let regUser: User = {
      firstName: firstname,
      secondName: lastname,
      login: login,
      mail: email,
      password: password
    };

    this.authenticationService.register(regUser)
    .subscribe(
      data => {
        this.alertService.success('Registration successful', true);
        this.router.navigate(['api/v1/login']);
      },
      error => {
        this.alertService.error("Error while registration!");
        console.log(error);
      });
  }

}
