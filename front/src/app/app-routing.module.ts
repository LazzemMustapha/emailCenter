import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { DashboardAdminComponent } from './component/dashboard-admin/dashboard-admin.component';
import { DashboardUserComponent } from './component/dashboard-user/dashboard-user.component';

import { Register1Component } from './component/register1/register1.component';

import { AddUserComponent } from './component/dashboard-admin/add-user/add-user.component';
import { EmailComponent } from './component/dashboard-user/email/email.component';
import { ListEmailComponent } from './component/dashboard-user/list-email/list-email.component';
import { ScanEmailComponent } from './component/dashboard-admin/scan-email/scan-email.component';
import { AccueilleComponent } from './component/accueille/accueille.component';
import { ArchivedEmailComponent } from './component/dashboard-user/archived-email/archived-email.component';
import { UserListComponent } from './component/dashboard-admin/user-list/user-list.component';
import { ArchivingRuleComponent } from './component/dashboard-user/archiving-rule/archiving-rule.component';
import { AuthGuardLoggedIn } from './component/guard/auth.guard';
import { ErreurComponent } from './component/erreur/erreur.component';
import { EmailAccountComponent } from './component/dashboard-user/email-account/email-account.component';
import { ComptedashboardComponent } from './component/comptedashboard/comptedashboard.component';
import { ListeemailComponent } from './component/listeemail/listeemail.component';
import { ArchivComponent } from './component/archiv/archiv.component';


const routes: Routes = [ 
  {path: 'erreur', component: ErreurComponent},
  { path: "", redirectTo: "login", pathMatch: "full" },
  { path: "dash/:id", component: ComptedashboardComponent,
    children:[  {path:"listeemail/:id" , component:ListeemailComponent},
      {path:"archivingRulee/:id" , component:ArchivingRuleComponent},
      {path:"emailarchivée/:id",component:ArchivComponent},

    ]

  },
  {path:"archivingRule/:id",component:ArchivingRuleComponent},
  {path:"listeemail/:id" , component:ListeemailComponent},
  {path:"accueille",component:AccueilleComponent},
  {path:"login" , component:LoginComponent},
  {path:"register" , component:RegisterComponent,canActivate:[AuthGuardLoggedIn],data:{role:['ADMIN']}},
  {path:"register1" , component:Register1Component,},
  {path:"admin" , component:DashboardAdminComponent,canActivate:[AuthGuardLoggedIn],data:{role:['ADMIN']},
  children:[{path:"listuser",component:UserListComponent,canActivate:[AuthGuardLoggedIn],data:{role:['ADMIN']}},
  {path:"adduser",component:AddUserComponent,canActivate:[AuthGuardLoggedIn],data:{role:['ADMIN']}},
  {path:"scan",component:ScanEmailComponent,canActivate:[AuthGuardLoggedIn],data:{role:['ADMIN']}},
  
  

  ]

  },
  {path:"user" , component:DashboardUserComponent ,
    children:[{path:"listemail/:id" , component:ListEmailComponent},
    
      {path:"email/:id",component:EmailComponent},
      {path:"emailarchivé",component:ArchivedEmailComponent},
      {path:"archivingRule/:id",component:ArchivingRuleComponent},
      {path:"account",component:EmailAccountComponent},
     // {path:"accueille",component:AccueilleComponent},
     // { path: "", redirectTo: "accueille", pathMatch: "full" }
    ]
  },
 
  
 
  


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})



export class AppRoutingModule { }
