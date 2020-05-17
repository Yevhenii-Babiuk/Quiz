import {Component, OnInit} from '@angular/core';
import {User} from "../../core/models/user";
import {ProfileService} from "../../core/services/profile.service";
import {Role} from "../../core/models/role";
import {SecurityService} from "../../core/services/security.service";
import {Imaged} from "../../core/models/imaged";
import {ActivatedRoute} from "@angular/router";
import {AlertService} from "../../core/services/alert.service";
import {AchievementService} from "../../core/services/achievement.service";
import {Status} from "../../core/models/Status";


@Component({
  selector: 'app-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.scss']
})

export class ViewProfile implements OnInit {
  loadedPhoto:boolean;
  updated: boolean;
  userData: User;
  id: number;
  role: Role;
  roleEnum = Role;
  isOwn: boolean;
  isFriend: boolean;
  visitorId: number;
  isActivated: boolean;
  statusEnum = Status;

  constructor(
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private securityService: SecurityService,
    private alertService:AlertService,
    private achievementService: AchievementService
  ) {
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('userId'));
    if (this.id) {
      this.visitorId = this.securityService.getCurrentId();
      if (this.visitorId != this.id) this.isOwn = false;
      else this.isOwn = true;
      this.profileService.checkFriendship(this.id, this.visitorId).subscribe(data => {
        this.isFriend = data;
      });
    } else {
      this.id = this.securityService.getCurrentId();
      this.isOwn = true;
    }

    this.getUser();
  }

  private getUser() {
    this.profileService.getUser(this.id).subscribe(data => {
      this.userData = data;
      this.role = this.securityService.getCurrentRole();
      this.isActivate();
    });
  }

  processFile(imageInput: any, imaged: Imaged) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      imaged.image.src = event.target.result;
      this.updated = true;
      this.loadedPhoto=true;
      this.profileService.putImage(file).subscribe(
        id => {
          console.log("id = " + id);
          if (typeof id === "number") {
            imaged.imageId = id;
          }
        },
        error => {
          console.log(error);
        });
    });
    reader.readAsDataURL(file);
  }

  update() {
    this.profileService.updateUserPhoto(this.userData).subscribe(
      get => {
      },
      error => {
        console.log(error);
      });

    this.loadedPhoto=false;
    this.alertService.success("Photo is changed");

  }

  friendship() {
    if (this.isFriend)
      this.profileService.removeFriend(this.visitorId, this.id).subscribe(data => {
        this.isFriend = false;
      });
    else
      this.profileService.addFriend(this.visitorId, this.id).subscribe(data => {
        this.isFriend = true
      });
  }

  recalculateAchievement() {
    this.achievementService.recalculateAchievements().subscribe();
  }

  isActivate(){
    if(this.userData.status &&
      this.userData.status==this.statusEnum.ACTIVATED){
      this.isActivated = true;
    }else {
      this.isActivated = false;
    }
  }

  changeStatus() {
    this.isActivated = !this.isActivated;
      let status: Status;
      if(this.isActivated){
        status = Status.ACTIVATED;
      }else{
        status = Status.UNACTIVATED;
      }
      this.userData.status=status;
      this.profileService.changeStatus(this.userData.id, status)
        .subscribe();
  }

}
