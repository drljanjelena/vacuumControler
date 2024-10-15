import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AllUsersComponent } from './components/all-users/all-users.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from '../guards/auth.guard';
import { NewGuard } from '../guards/new.guard';
import { EditGuard } from '../guards/edit.guard';
import { AppComponent } from './components/app/app.component';
import { VacuumSearchComponent } from './components/vacuum-search/vacuum-search.component';
import { HistoryComponent } from './components/history/history.component';
import { AddVacuumComponent } from './components/add-vacuum/add-vacuum.component';
import { SearchGuard } from 'src/guards/search.guard';
import { AddGuard } from 'src/guards/add.guard';
import { SchedulerFormComponent } from './components/scheduler-form/scheduler-form.component'

const routes: Routes = [
  {
    path: "",
    component: AllUsersComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "all",
    component: AllUsersComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "add",
    component: AddUserComponent,
    canActivate: [NewGuard]

  },
  { 
    path: 'users/:id/edit', 
    component: EditUserComponent,
    canActivate: [AuthGuard, EditGuard]
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "search",
    component: VacuumSearchComponent,
    canActivate: [SearchGuard]

  },
  {
    path: "history",
    component: HistoryComponent,
  },
  {
    path: "add-vacuum",
    component: AddVacuumComponent,
    canActivate: [AddGuard]
  },
  {
    path: "schedule/:vacuumId",
    component: SchedulerFormComponent,
    canActivate: [AddGuard]
  },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
