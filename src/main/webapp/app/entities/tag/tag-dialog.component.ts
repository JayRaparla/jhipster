import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Tag } from './tag.model';
import { TagPopupService } from './tag-popup.service';
import { TagService } from './tag.service';
import { Transaction, TransactionService } from '../transaction';

@Component({
    selector: 'jhi-tag-dialog',
    templateUrl: './tag-dialog.component.html'
})
export class TagDialogComponent implements OnInit {

    tag: Tag;
    authorities: any[];
    isSaving: boolean;

    transactions: Transaction[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private tagService: TagService,
        private transactionService: TransactionService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.transactionService.query().subscribe(
            (res: Response) => { this.transactions = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear () {
        this.activeModal.dismiss('cancel');
    }

    save () {
        this.isSaving = true;
        if (this.tag.id !== undefined) {
            this.tagService.update(this.tag)
                .subscribe((res: Tag) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.tagService.create(this.tag)
                .subscribe((res: Tag) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess (result: Tag) {
        this.eventManager.broadcast({ name: 'tagListModification', content: 'OK'});
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

    trackTransactionById(index: number, item: Transaction) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-tag-popup',
    template: ''
})
export class TagPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private tagPopupService: TagPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.tagPopupService
                    .open(TagDialogComponent, params['id']);
            } else {
                this.modalRef = this.tagPopupService
                    .open(TagDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
