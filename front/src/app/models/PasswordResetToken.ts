// password-reset-token.ts
import { USER } from '../services/regres';


export class PasswordResetToken {
  id: number;
  token: string;
  user: USER;
  expiryDate: Date;

  constructor(id: number, token: string, user: USER, expiryDate: Date) {
    this.id = id;
    this.token = token;
    this.user = user;
    this.expiryDate = expiryDate;
  }

  // Méthode pour mettre à jour le token
  updateToken(token: string): void {
    this.token = token;
    this.expiryDate = this.calculateExpiryDate();
  }

  // Méthode pour calculer la date d'expiration
  private calculateExpiryDate(): Date {
    const expiryTimeInMinutes = 60 * 24;
    const expiryDate = new Date();
    expiryDate.setTime(expiryDate.getTime() + expiryTimeInMinutes * 60 * 1000);
    return expiryDate;
  }
}
