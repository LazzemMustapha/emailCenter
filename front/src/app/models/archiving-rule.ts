import { Account } from './account';

export class ArchivingRule {
  id: number;
  account: Account;
  retentionPeriod: RetentionPeriodEnum;

  constructor(id: number, account: Account, retentionPeriod: RetentionPeriodEnum) {
    this.id = id;
    this.account = account;
    this.retentionPeriod = retentionPeriod;
  }

}
export enum RetentionPeriodEnum {
  DAILY = 'DAILY',
  WEEKLY = 'WEEKLY',
  MONTHLY = 'MONTHLY'
}
