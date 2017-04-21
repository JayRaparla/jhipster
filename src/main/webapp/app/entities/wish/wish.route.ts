import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { WishComponent } from './wish.component';
import { WishDetailComponent } from './wish-detail.component';
import { WishPopupComponent } from './wish-dialog.component';
import { WishDeletePopupComponent } from './wish-delete-dialog.component';

import { Principal } from '../../shared';


export const wishRoute: Routes = [
  {
    path: 'wish',
    component: WishComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Wishes'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'wish/:id',
    component: WishDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Wishes'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const wishPopupRoute: Routes = [
  {
    path: 'wish-new',
    component: WishPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Wishes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'wish/:id/edit',
    component: WishPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Wishes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'wish/:id/delete',
    component: WishDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Wishes'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
