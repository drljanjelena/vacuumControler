import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { BackendService } from 'src/service/backend.service';
import { User } from 'src/model';
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {

  form: FormGroup;
  
  constructor(private fb: FormBuilder, private router : Router, private backendService: BackendService) {
    this.form = this.fb.group({
      id: [''],
      firstName: [''],
      lastName: [''],
      email: [''],
      password: [''],
      permissions: [''],
      create: [false],
      read: [false],
      update: [false],
      delete: [false]
    });
  }

  onSubmit() {

    const jwt = localStorage.getItem("authToken") || ""

    console.log(this.checkPermissions());

    const salt = bcrypt.genSaltSync(10);
    const pass = bcrypt.hashSync(this.form.get('password')?.value, salt);

    const user: User = {
      userId: "0",
      firstName: this.form.get('firstName')?.value,
      lastName: this.form.get('lastName')?.value,
      email: this.form.get('email')?.value,
      password: pass,
      permissions: this.checkPermissions().join(','),
    };
    this.backendService.addUser(user, jwt)
      .subscribe(
      (data) => {
        alert("User added successfully");
        this.router.navigate(['all']);
      },
      (error) => {
        if (error.error) {
          alert(error.error.message);
        } else {
          console.error('Default error message:', 'Something went wrong.');
        }
      }
    );
 }

  checkPermissions(){
    const permissionsList = [];

    if (this.form.get('create')?.value) {
      permissionsList.push('can_create_users');
    }
    if (this.form.get('read')?.value) {
      permissionsList.push('can_read_users');
    }
    if (this.form.get('update')?.value) {
      permissionsList.push('can_update_users');
    }
    if (this.form.get('delete')?.value) {
      permissionsList.push('can_delete_users');
    }
    return permissionsList;
  }

}
