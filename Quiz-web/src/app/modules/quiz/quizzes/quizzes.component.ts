import {Component, HostListener, OnInit} from '@angular/core';
import {QuizzesService} from "../../core/services/quizzes.service";
import {Quiz} from "../../core/models/quiz";
import {KeywordFilter} from "./vertical-filter-bar/keyword-filter/keyword-filter.model";
import {CheckboxFilter} from "./vertical-filter-bar/checkbox-filter/checkbox-filter.model";
import {FBFilter} from "./vertical-filter-bar/fb-filter.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {DateFilter} from "./vertical-filter-bar/date-filter/date-filter.model";
import {countOnPage} from "../../../../environments/environment.prod";
import {TagFilter} from "./vertical-filter-bar/tag-filter/tag-filter.model";
import {SecurityService} from "../../core/services/security.service";
import {TranslateService} from '@ngx-translate/core';
import {AuthenticationService} from "../../core/services/authentication.service";

@Component({
  selector: 'app-quizzes',
  templateUrl: './quizzes.component.html'
})
export class QuizzesComponent implements OnInit {
  quizzes: Quiz[] = [new Quiz(), new Quiz(), new Quiz(), new Quiz(), new Quiz()];
  tags: string[] = [];
  categories: string[] = [];
  quizStatuses: string[] = [
    'ACTIVATED',
    'UNPUBLISHED',
    'UNVALIDATED',
    'UNSAVED',
    'DEACTIVATED'
  ];
  isWaiting: boolean = false;

  params: string;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private quizzesService: QuizzesService,
    public translate: TranslateService,
    private securityService: SecurityService
  ) {
  }

  verticalBarFilters: FBFilter[] = [
    new KeywordFilter('quizName', 'Quiz name'),
    new KeywordFilter('authorName', 'Author name'),
    new CheckboxFilter('category', 'Categories', this.categories),
    new TagFilter('tag', 'Tags', this.tags),
    new DateFilter("date", "Date", new Date(2020, 3, 20), new Date())
  ]

  quizStatusesFilter: FBFilter = new CheckboxFilter('status', 'Status', this.quizStatuses);


  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {

    if (document.documentElement.scrollHeight - document.documentElement.scrollTop -
      document.documentElement.clientHeight < 40) {
      this.getQuizzes();
    }

  }

  getQuizzes(): void {
    if (this.isWaiting) {
      return;
    }
    this.isWaiting = true;
    this.quizzesService.getQuizzes(this.params, this.quizzes.length)
      .subscribe(
        quizzes => {
          if (quizzes.length == countOnPage) {
            this.isWaiting = false;
          }
          this.quizzes = this.quizzes.concat(quizzes);
        },
        err => {
          console.log(err);
        })

  }

  getTags(): void {
    this.quizzesService.getTags()
      .subscribe(
        tags => {
          tags.forEach(function (value) {
            this.push(value.name);
          }, this.tags);

        },
        err => {
          console.log(err);
        })
  }

  getCategories(): void {
    this.quizzesService.getCategories()
      .subscribe(
        category => {
          category.forEach(function (value) {
            this.push(value.name);
          }, this.categories);

        },
        err => {
          console.log(err);
        })
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(_params => {
      this.quizzes = [];
      this.isWaiting = false;
      this.params = "?";
      if (!this.securityService.getCurrentRole() || this.securityService.getCurrentRole()=='USER'){
        this.params +='status=ACTIVATED&'
      }
      if (this.router.url.indexOf("?")!=-1){
        this.params += this.router.url.substring(this.router.url.indexOf("?") + 1);
      }

      this.getQuizzes();

    });

    if(this.securityService.getCurrentRole() && this.securityService.getCurrentRole()!='USER'){
      this.verticalBarFilters.push(this.quizStatusesFilter);
    }

    this.getTags();
    this.getCategories();

  }

}
