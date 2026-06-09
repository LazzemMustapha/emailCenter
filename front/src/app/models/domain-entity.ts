export class DomainEntity {
  id: number;
  domainName: string;

  constructor(id: number, domainName: string) {
    this.id = id;
    this.domainName = domainName;
  }
}
