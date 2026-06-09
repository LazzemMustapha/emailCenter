import { Component, OnInit } from '@angular/core';
import { ScanRule } from '../../../models/scan-rule';
import { ScanRuleService } from '../../../services/scan-rule.service';

@Component({
  selector: 'app-scan-email',
  templateUrl: './scan-email.component.html',
  styleUrls: ['./scan-email.component.css']
})
export class ScanEmailComponent implements OnInit {
  scanRules: ScanRule[] = [];
  newScanRule: ScanRule = new ScanRule(0, 0);
  selectedScanRule: ScanRule | null = null;

  // Temporary properties for editing
  editHour: number | null = null;
  editMinute: number | null = null;

  constructor(private scanRuleService: ScanRuleService) { }

  ngOnInit(): void {
    this.loadScanRules();
  }

  loadScanRules(): void {
    this.scanRuleService.getAllScanRules().subscribe(data => {
      this.scanRules = data;
    });
  }

  addScanRule(): void {
    this.scanRuleService.addScanRule(this.newScanRule).subscribe(() => {
      this.loadScanRules();
      this.newScanRule = new ScanRule(0, 0);
    });
    const closeModalBtn = document.querySelector('#addScanModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
  }

  updateScanRule(): void {
    if (this.selectedScanRule !== null) {
      this.selectedScanRule.hour = this.editHour!;
      this.selectedScanRule.minute = this.editMinute!;
      this.scanRuleService.updateScanRule(this.selectedScanRule.id, this.selectedScanRule).subscribe(() => {
        this.loadScanRules();
        this.selectedScanRule = null;
      });
      const closeModalBtn = document.querySelector('#editScanModal .btn-close') as HTMLElement;
      if (closeModalBtn) closeModalBtn.click();
    }
  }

  deleteScanRule(id: number): void {
    this.scanRuleService.deleteScanRule(id).subscribe(() => {
      this.loadScanRules();
    });
  }

  selectScanRule(scanRule: ScanRule): void {
    this.selectedScanRule = { ...scanRule };
    this.editHour = scanRule.hour;
    this.editMinute = scanRule.minute;
    
  }

  
 
}
