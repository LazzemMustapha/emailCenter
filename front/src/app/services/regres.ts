import { Account } from "../models/account";


export interface USER {

    email: any;
    username: any;
    id: any;
    password: any;
    role: any;
    //authorities: Authority[]; 
    accounts: Account[];

   
    
  }

  export interface account {
id:any
serveur : any ;
port : any
email:any;
password: any ;
user:USER;


  }
  export interface accountt {
    
    serveur : any ;
    port : any
    email:any;
    password: any ;
    
      } 
 // export interface Authority {
   // authority: string;
  //}
  export enum Role {
    USER = 'USER',
    ADMIN = 'ADMIN',
    MANAGER = 'MANAGER'
  }
  
  export const Permission = {
    ADMIN_READ: 'ADMIN_READ',
    ADMIN_UPDATE: 'ADMIN_UPDATE',
    ADMIN_DELETE: 'ADMIN_DELETE',
    ADMIN_CREATE: 'ADMIN_CREATE',
    MANAGER_READ: 'MANAGER_READ',
    MANAGER_UPDATE: 'MANAGER_UPDATE',
    MANAGER_DELETE: 'MANAGER_DELETE',
    MANAGER_CREATE: 'MANAGER_CREATE'
  }
  export const defaultUser: USER = {
    id: 0,
    username: ''
    // initialize other properties with default values
    ,




    email: undefined,
    password: undefined,
    role: undefined,
    //authorities: [],
    accounts: []
  };
  