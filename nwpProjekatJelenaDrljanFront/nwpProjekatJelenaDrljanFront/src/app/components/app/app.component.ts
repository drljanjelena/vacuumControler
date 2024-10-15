import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'nwp-project3-front';
 
  constructor( private auth: AuthService, private route: Router) {

  }
  ngOnInit(): void {
    const token = localStorage.getItem('authToken');

    if (token) {
      this.auth.login(token);
    }
  }

  logout(){
    this.auth.logout();
    this.route.navigate(['/login']);
  }

}
