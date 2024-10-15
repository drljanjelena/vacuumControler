import { Component, inject } from '@angular/core';
import { ErrorMessage } from 'src/model';
import { ErrorServiceService } from 'src/service/error.service';


@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent {
  errorHistory: ErrorMessage[] = [];

  constructor(private errorService: ErrorServiceService) { }

  ngOnInit(): void {
    this.fetchErrorHistory();
  }

  fetchErrorHistory(): void {
    var token = localStorage.getItem("authToken") || "";

    this.errorService.getErrors(token).subscribe(
      (data) => {
        this.errorHistory = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

}
