import { NgModule,CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // Importez FormsModule ici

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { DashboardAdminComponent } from './component/dashboard-admin/dashboard-admin.component';
import { DashboardUserComponent } from './component/dashboard-user/dashboard-user.component';
import { ReactiveFormsModule } from '@angular/forms'; // Supprimez cette ligne

import { HttpClientModule } from '@angular/common/http';
import { Register1Component } from './component/register1/register1.component';
import { ListEmailComponent } from './component/dashboard-user/list-email/list-email.component';
import { EmailComponent } from './component/dashboard-user/email/email.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { ScanEmailComponent } from './component/dashboard-admin/scan-email/scan-email.component';
import { AccueilleComponent } from './component/accueille/accueille.component';
import { ArchivedEmailComponent } from './component/dashboard-user/archived-email/archived-email.component';
import { AddUserComponent } from './component/dashboard-admin/add-user/add-user.component';
import { UserListComponent } from './component/dashboard-admin/user-list/user-list.component';
import { ArchivingRuleComponent } from './component/dashboard-user/archiving-rule/archiving-rule.component';
import { ErreurComponent } from './component/erreur/erreur.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { EmailAccountComponent } from './component/dashboard-user/email-account/email-account.component';
import { ComptedashboardComponent } from './component/comptedashboard/comptedashboard.component';
import { ListeemailComponent } from './component/listeemail/listeemail.component';
import { ArchivruleComponent } from './component/archivrule/archivrule.component';
import { ArchivComponent } from './component/archiv/archiv.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    DashboardAdminComponent,
    DashboardUserComponent,
    ListEmailComponent,
    EmailComponent,
    Register1Component,
    ScanEmailComponent,
    AccueilleComponent,
    ArchivedEmailComponent,
    AddUserComponent,
    UserListComponent,
    ArchivingRuleComponent,
    ErreurComponent,
    EmailAccountComponent,
    ComptedashboardComponent,
    ListeemailComponent,
    ArchivruleComponent,
    ArchivComponent
    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule, // Ajoutez FormsModule ici
    HttpClientModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    BrowserAnimationsModule, 
    ToastrModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }

