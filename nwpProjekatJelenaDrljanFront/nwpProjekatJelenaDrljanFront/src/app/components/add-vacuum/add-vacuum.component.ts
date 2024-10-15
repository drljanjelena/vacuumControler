import { Component } from '@angular/core';
import { VacuumService } from 'src/service/vacuum.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/service/auth.service';
import { NgbCalendar, NgbDate, NgbDateParserFormatter, NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-add-vacuum',
  templateUrl: './add-vacuum.component.html',
  styleUrls: ['./add-vacuum.component.css']
})
export class AddVacuumComponent {

  form: FormGroup;

  constructor(private fb: FormBuilder, private vacuumService: VacuumService){
    this.form = this.fb.group({
      email : [''],
    });
  }

  onSubmit() {
    var token = localStorage.getItem("authToken") || "";
    this.vacuumService.add( 
      {
        name: this.form.get('email')?.value,
      }, token)
      .subscribe(
      (data) => {
        alert("Added");
      },
      (error) => {
        console.error(error);
      }
    );
  }

}
