import { Attachment } from './attachment';
import { Account } from './account';
import { DomainEntity } from './domain-entity';

export class Email {
  id: number;
  sender: string;
  recipients: string;
  subject: string;
  body: string;
  date: string; // Utilisation de string pour LocalDate
  attachments: Attachment[];
  account: Account;
  domainEntities: DomainEntity[];
  selected?: boolean;

  constructor(
    id: number,
    sender: string,
    recipients: string,
    subject: string,
    body: string,
    date: string,
    attachments: Attachment[],
    account: Account,
    domainEntities: DomainEntity[]
  ) {
    this.id = id;
    this.sender = sender;
    this.recipients = recipients;
    this.subject = subject;
    this.body = body;
    this.date = date;
    this.attachments = attachments;
    this.account = account;
    this.domainEntities = domainEntities;
  }
}
