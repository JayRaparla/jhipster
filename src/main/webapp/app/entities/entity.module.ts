import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyappTransactionModule } from './transaction/transaction.module';
import { MyappRemarkModule } from './remark/remark.module';
import { MyappTagModule } from './tag/tag.module';
import { MyappWishListModule } from './wish-list/wish-list.module';
import { MyappWishModule } from './wish/wish.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MyappTransactionModule,
        MyappRemarkModule,
        MyappTagModule,
        MyappWishListModule,
        MyappWishModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyappEntityModule {}
