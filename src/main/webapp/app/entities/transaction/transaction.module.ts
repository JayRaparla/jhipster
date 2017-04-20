import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyappSharedModule } from '../../shared';
import { MyappAdminModule } from '../../admin/admin.module';

import {
    TransactionService,
    TransactionPopupService,
    TransactionComponent,
    TransactionDetailComponent,
    TransactionDialogComponent,
    TransactionPopupComponent,
    TransactionDeletePopupComponent,
    TransactionDeleteDialogComponent,
    transactionRoute,
    transactionPopupRoute,
} from './';

let ENTITY_STATES = [
    ...transactionRoute,
    ...transactionPopupRoute,
];

@NgModule({
    imports: [
        MyappSharedModule,
        MyappAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransactionComponent,
        TransactionDetailComponent,
        TransactionDialogComponent,
        TransactionDeleteDialogComponent,
        TransactionPopupComponent,
        TransactionDeletePopupComponent,
    ],
    entryComponents: [
        TransactionComponent,
        TransactionDialogComponent,
        TransactionPopupComponent,
        TransactionDeleteDialogComponent,
        TransactionDeletePopupComponent,
    ],
    providers: [
        TransactionService,
        TransactionPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyappTransactionModule {}
