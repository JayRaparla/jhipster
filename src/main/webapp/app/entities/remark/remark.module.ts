import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyappSharedModule } from '../../shared';

import {
    RemarkService,
    RemarkPopupService,
    RemarkComponent,
    RemarkDetailComponent,
    RemarkDialogComponent,
    RemarkPopupComponent,
    RemarkDeletePopupComponent,
    RemarkDeleteDialogComponent,
    remarkRoute,
    remarkPopupRoute,
} from './';

let ENTITY_STATES = [
    ...remarkRoute,
    ...remarkPopupRoute,
];

@NgModule({
    imports: [
        MyappSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RemarkComponent,
        RemarkDetailComponent,
        RemarkDialogComponent,
        RemarkDeleteDialogComponent,
        RemarkPopupComponent,
        RemarkDeletePopupComponent,
    ],
    entryComponents: [
        RemarkComponent,
        RemarkDialogComponent,
        RemarkPopupComponent,
        RemarkDeleteDialogComponent,
        RemarkDeletePopupComponent,
    ],
    providers: [
        RemarkService,
        RemarkPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyappRemarkModule {}
