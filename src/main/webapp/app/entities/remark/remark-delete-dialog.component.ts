import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Remark } from './remark.model';
import { RemarkPopupService } from './remark-popup.service';
import { RemarkService } from './remark.service';

@Component({
    selector: 'jhi-remark-delete-dialog',
    templateUrl: './remark-delete-dialog.component.html'
})
export class RemarkDeleteDialogComponent {

    remark: Remark;

    constructor(
        private remarkService: RemarkService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear () {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete (id: number) {
        this.remarkService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'remarkListModification',
                content: 'Deleted an remark'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-remark-delete-popup',
    template: ''
})
export class RemarkDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor (
        private route: ActivatedRoute,
        private remarkPopupService: RemarkPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe(params => {
            this.modalRef = this.remarkPopupService
                .open(RemarkDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
