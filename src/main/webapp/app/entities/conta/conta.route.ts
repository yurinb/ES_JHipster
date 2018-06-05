import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ContaComponent } from './conta.component';
import { ContaDetailComponent } from './conta-detail.component';
import { ContaPopupComponent } from './conta-dialog.component';
import { ContaDeletePopupComponent } from './conta-delete-dialog.component';

export const contaRoute: Routes = [
    {
        path: 'conta',
        component: ContaComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'esJhipsterApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'conta/:id',
        component: ContaDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'esJhipsterApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contaPopupRoute: Routes = [
    {
        path: 'conta-new',
        component: ContaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'esJhipsterApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conta/:id/edit',
        component: ContaPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'esJhipsterApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'conta/:id/delete',
        component: ContaDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'esJhipsterApp.conta.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
