import { Account } from "./account";
import { Attachment } from "./attachment";

export class ArchivedEmail {
    id!: number;
    sender: string;
    recipients: string;
    subject: string;
    body: string;
    date: string;
    attachments: Attachment[];
    account: Account;
    selected?: boolean; 
  
    constructor(sender: string, recipients: string, subject: string, body: string, date: string, attachments: Attachment[], account: Account) {
      this.sender = sender;
      this.recipients = recipients;
      this.subject = subject;
      this.body = body;
      this.date = date;
      this.attachments = attachments;
      this.account = account;
    }
  }