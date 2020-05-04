import {Component, Input, OnInit} from '@angular/core';
import {GameResults, SingleResult} from "../../../core/models/gameResults";
import {Sort} from "@angular/material/sort";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
})
export class ResultsComponent implements OnInit {

  @Input()
  gameResults: GameResults;
  sortedData: SingleResult[];


  constructor() {
  }

  ngOnInit(): void {
    this.sortedData = this.gameResults.singleResult;
  }

  sortData(sort: Sort) {
    const data = this.gameResults.singleResult.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'login':
          return compare(a.login, b.login, isAsc);
        case 'score':
          return compare(a.score, b.score, isAsc);
        default:
          return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
