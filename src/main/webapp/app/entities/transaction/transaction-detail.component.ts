import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager   } from 'ng-jhipster';

import { Transaction } from './transaction.model';
import { TransactionService } from './transaction.service';

@Component({
    selector: 'jhi-transaction-detail',
    templateUrl: './transaction-detail.component.html'
})
export class TransactionDetailComponent implements OnInit, OnDestroy {

    transaction: Transaction;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private transactionService: TransactionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInTransactions();
    }

    load (id) {
        this.transactionService.find(id).subscribe(transaction => {
            this.transaction = transaction;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTransactions() {
        this.eventSubscriber = this.eventManager.subscribe('transactionListModification', response => this.load(this.transaction.id));
    }

}
