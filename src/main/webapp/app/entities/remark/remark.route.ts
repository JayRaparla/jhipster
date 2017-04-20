import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RemarkComponent } from './remark.component';
import { RemarkDetailComponent } from './remark-detail.component';
import { RemarkPopupComponent } from './remark-dialog.component';
import { RemarkDeletePopupComponent } from './remark-delete-dialog.component';

import { Principal } from '../../shared';


export const remarkRoute: Routes = [
  {
    path: 'remark',
    component: RemarkComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Remarks'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'remark/:id',
    component: RemarkDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Remarks'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const remarkPopupRoute: Routes = [
  {
    path: 'remark-new',
    component: RemarkPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Remarks'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'remark/:id/edit',
    component: RemarkPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Remarks'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'remark/:id/delete',
    component: RemarkDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Remarks'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
