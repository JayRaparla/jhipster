import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyappSharedModule } from '../../shared';
import { MyappAdminModule } from '../../admin/admin.module';

import {
    WishService,
    WishPopupService,
    WishComponent,
    WishDetailComponent,
    WishDialogComponent,
    WishPopupComponent,
    WishDeletePopupComponent,
    WishDeleteDialogComponent,
    wishRoute,
    wishPopupRoute,
} from './';

let ENTITY_STATES = [
    ...wishRoute,
    ...wishPopupRoute,
];

@NgModule({
    imports: [
        MyappSharedModule,
        MyappAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        WishComponent,
        WishDetailComponent,
        WishDialogComponent,
        WishDeleteDialogComponent,
        WishPopupComponent,
        WishDeletePopupComponent,
    ],
    entryComponents: [
        WishComponent,
        WishDialogComponent,
        WishPopupComponent,
        WishDeleteDialogComponent,
        WishDeletePopupComponent,
    ],
    providers: [
        WishService,
        WishPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyappWishModule {}
