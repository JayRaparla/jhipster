import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Remark } from './remark.model';
import { RemarkService } from './remark.service';
@Injectable()
export class RemarkPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private remarkService: RemarkService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.remarkService.find(id).subscribe(remark => {
                remark.date = this.datePipe
                    .transform(remark.date, 'yyyy-MM-ddThh:mm');
                this.remarkModalRef(component, remark);
            });
        } else {
            return this.remarkModalRef(component, new Remark());
        }
    }

    remarkModalRef(component: Component, remark: Remark): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.remark = remark;
        modalRef.result.then(result => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
