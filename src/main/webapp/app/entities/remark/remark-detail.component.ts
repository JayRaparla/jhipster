import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager   } from 'ng-jhipster';

import { Remark } from './remark.model';
import { RemarkService } from './remark.service';

@Component({
    selector: 'jhi-remark-detail',
    templateUrl: './remark-detail.component.html'
})
export class RemarkDetailComponent implements OnInit, OnDestroy {

    remark: Remark;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private remarkService: RemarkService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInRemarks();
    }

    load (id) {
        this.remarkService.find(id).subscribe(remark => {
            this.remark = remark;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRemarks() {
        this.eventSubscriber = this.eventManager.subscribe('remarkListModification', response => this.load(this.remark.id));
    }

}
