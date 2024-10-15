import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/service/auth.service';
import { BackendService } from 'src/service/backend.service';
import { User } from 'src/model';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit{

  users: User[] = [];
  token: string ='';

  constructor(private backendService: BackendService, private auth: AuthService, private route: Router) {

  }

  ngOnInit(): void {
    this.token = localStorage.getItem("authToken") || "";

    this.backendService.getUsers(this.token).subscribe(
      (data) => {
        this.users = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  logout(){
    this.auth.logout();
    this.route.navigate(['/login']);
  }

  deleteUser(userId: string) {
    this.backendService.deleteUser(userId, this.token).subscribe(
      (data) => {
        location.reload();
      },
      (error) => {
        console.error(error);
      }
    );
  }

  userCanDelete(): boolean {
    if(this.auth.canDelete())
      return true; 
    return false;
  }

  userCanEdit(): boolean {
    if(this.auth.canUpdate())
      return true; 
    return false;
  }

}
