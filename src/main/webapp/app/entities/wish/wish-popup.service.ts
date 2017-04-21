import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { Wish } from './wish.model';
import { WishService } from './wish.service';
@Injectable()
export class WishPopupService {
    private isOpen = false;
    constructor (
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private wishService: WishService

    ) {}

    open (component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.wishService.find(id).subscribe(wish => {
                if (wish.addedOn) {
                    wish.addedOn = {
                        year: wish.addedOn.getFullYear(),
                        month: wish.addedOn.getMonth() + 1,
                        day: wish.addedOn.getDate()
                    };
                }
                wish.completeBy = this.datePipe
                    .transform(wish.completeBy, 'yyyy-MM-ddThh:mm');
                this.wishModalRef(component, wish);
            });
        } else {
            return this.wishModalRef(component, new Wish());
        }
    }

    wishModalRef(component: Component, wish: Wish): NgbModalRef {
        let modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.wish = wish;
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
