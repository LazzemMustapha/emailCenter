import { Component, OnInit } from '@angular/core';
import { ArchivingRule, RetentionPeriodEnum } from '../../../models/archiving-rule';
import { Account } from '../../../models/account';
import { ArchivingRuleService } from '../../../services/archivingrule.service';
import { TokenStorageService } from '../../../services/token-storage.service';
import { account } from '../../../services/regres';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-archiving-rule',
  templateUrl: './archiving-rule.component.html',
  styleUrls: ['./archiving-rule.component.css']
})
export class ArchivingRuleComponent implements OnInit {
  id = this.route.snapshot.params['id']
  archivingRules: ArchivingRule[] = [];
  newArchivingRule: ArchivingRule = new ArchivingRule(0, new Account(this.id, '', '', '', '', {} as any), RetentionPeriodEnum.DAILY);
  editArchivingRule: ArchivingRule = new ArchivingRule(0, new Account(0, '', '', '', '', {} as any), RetentionPeriodEnum.DAILY);
  retentionPeriods = Object.values(RetentionPeriodEnum);
  
accountid: account[] = []
a !:account;
  constructor(private archivingRuleService: ArchivingRuleService, private route : ActivatedRoute , private tokens : TokenStorageService) { }

  ngOnInit(): void {
    //this.loadArchivingRules();accountId?: numberaccountId: numberaccountId: number
    this.loadArchivingRulesByAccountId();
  }

  /*loadArchivingRules(): void {
    this.archivingRuleService.getArchivingRules().subscribe((data: ArchivingRule[]) => {
      this.archivingRules = data;
    });
  }*/
  loadArchivingRulesByAccountId(): void {
    // Récupérer l'ID du compte à partir de votre application ou d'un service approprié
    // this.accountid = this.tokens.getemail(); // Remplacez 123 par l'ID du compte réel
console.log(this.accountid)
//for ( const aaa of this.accountid){


    // Utiliser la méthode pour récupérer les règles d'archivage par ID de compte
   this.archivingRuleService. getAllArchivingRulesByAccountId(this.id).subscribe((data: ArchivingRule[]) => {
      this.archivingRules = data;
    });
  }

  addArchivingRule(): void {
    this.archivingRuleService.addArchivingRule(this.newArchivingRule).subscribe(() => {
      //this.loadArchivingRules();
      this.loadArchivingRulesByAccountId();
      this.newArchivingRule = new ArchivingRule(0, new Account(0, '', '', '', '', {} as any), RetentionPeriodEnum.DAILY);
      const closeModalBtn = document.querySelector('#addArchivingRuleModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
    });
  }

  selectArchivingRule(archivingRule: ArchivingRule): void {
    this.editArchivingRule = { ...archivingRule };
  }

  updateArchivingRule(): void {
    this.archivingRuleService.updateArchivingRule(this.editArchivingRule.id, this.editArchivingRule).subscribe(() => {
      //this.loadArchivingRules();
       this.loadArchivingRulesByAccountId();
      const closeModalBtn = document.querySelector('#editArchivingRuleModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
    });
  }
}
