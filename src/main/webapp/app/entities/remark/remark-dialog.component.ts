import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Remark } from './remark.model';
import { RemarkPopupService } from './remark-popup.service';
import { RemarkService } from './remark.service';
import { Transaction, TransactionService } from '../transaction';

@Component({
    selector: 'jhi-remark-dialog',
    templateUrl: './remark-dialog.component.html'
})
export class RemarkDialogComponent implements OnInit {

    remark: Remark;
    authorities: any[];
    isSaving: boolean;

    transactions: Transaction[];
    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private remarkService: RemarkService,
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
        if (this.remark.id !== undefined) {
            this.remarkService.update(this.remark)
                .subscribe((res: Remark) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.remarkService.create(this.remark)
                .subscribe((res: Remark) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess (result: Remark) {
        this.eventManager.broadcast({ name: 'remarkListModification', content: 'OK'});
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
    selector: 'jhi-remark-popup',
    template: ''
})
export class RemarkPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private remarkPopupService: RemarkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            if ( params['id'] ) {
                this.modalRef = this.remarkPopupService
                    .open(RemarkDialogComponent, params['id']);
            } else {
                this.modalRef = this.remarkPopupService
                    .open(RemarkDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
