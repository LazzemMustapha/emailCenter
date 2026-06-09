import { USER ,defaultUser} from '../services/regres';



export class Account {
  id: number;
  password: string;
  email: string;
  port: string;
  serveur: string;
  
  user: USER;

  constructor(
    id: number = 0,
    password: string = '',
    email: string = '',
    port: string = '',
    serveur: string = '',
    user: USER = defaultUser
  ) {
    this.id = id;
    this.password = password;
    this.email = email;
    this.port = port;
    this.serveur = serveur;
    this.user = user;
  }
}
