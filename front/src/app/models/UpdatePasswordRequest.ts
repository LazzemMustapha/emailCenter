// update-password-request.ts
export class UpdatePasswordRequest {
    private resetToken: string;
    private newPassword: string;
  
    constructor(resetToken: string, newPassword: string) {
      this.resetToken = resetToken;
      this.newPassword = newPassword;
    }
  
   
  }
  