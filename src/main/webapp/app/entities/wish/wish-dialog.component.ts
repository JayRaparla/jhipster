import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Wish } from './wish.model';
import { WishPopupService } from './wish-popup.service';
import { WishService } from './wish.service';
import { WishList, WishListService } from '../wish-list';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-wish-dialog',
    templateUrl: './wish-dialog.component.html'
})
export class WishDialogComponent implements OnInit {

    wish: Wish;
    authorities: any[];
    isSaving: boolean;

    wishlists: WishList[];

    users: User[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private wishService: WishService,
        private wishListService: WishListService,
        private userService: UserService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.wishListService.query().subscribe(
            (res: Response) => { this.wishlists = res.json(); }, (res: Response) => this.onError(res.json()));
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.wish.id !== undefined) {
            this.wishService.update(this.wish)
                .subscribe((res: Wish) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.wishService.create(this.wish)
                .subscribe((res: Wish) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess (result: Wish) {
        this.eventManager.broadcast({ name: 'wishListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError (error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError (error) {
        this.alertService.error(error.message, null, null);
    }

    trackWishListById(index: number, item: WishList) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-wish-popup',
    template: ''
})
export class WishPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private wishPopupService: WishPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.wishPopupService
                    .open(WishDialogComponent, params['id']);
            } else {
                this.modalRef = this.wishPopupService
                    .open(WishDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
