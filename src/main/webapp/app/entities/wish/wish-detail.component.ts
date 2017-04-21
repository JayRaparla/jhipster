import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager   } from 'ng-jhipster';

import { Wish } from './wish.model';
import { WishService } from './wish.service';

@Component({
    selector: 'jhi-wish-detail',
    templateUrl: './wish-detail.component.html'
})
export class WishDetailComponent implements OnInit, OnDestroy {

    wish: Wish;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private wishService: WishService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInWishes();
    }

    load (id) {
        this.wishService.find(id).subscribe(wish => {
            this.wish = wish;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWishes() {
        this.eventSubscriber = this.eventManager.subscribe('wishListModification', response => this.load(this.wish.id));
    }

}
