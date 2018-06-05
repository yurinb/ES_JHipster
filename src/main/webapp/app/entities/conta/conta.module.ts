import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { EsJhipsterSharedModule } from '../../shared';
import { EsJhipsterAdminModule } from '../../admin/admin.module';
import {
    ContaService,
    ContaPopupService,
    ContaComponent,
    ContaDetailComponent,
    ContaDialogComponent,
    ContaPopupComponent,
    ContaDeletePopupComponent,
    ContaDeleteDialogComponent,
    contaRoute,
    contaPopupRoute,
} from './';

const ENTITY_STATES = [
    ...contaRoute,
    ...contaPopupRoute,
];

@NgModule({
    imports: [
        EsJhipsterSharedModule,
        EsJhipsterAdminModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContaComponent,
        ContaDetailComponent,
        ContaDialogComponent,
        ContaDeleteDialogComponent,
        ContaPopupComponent,
        ContaDeletePopupComponent,
    ],
    entryComponents: [
        ContaComponent,
        ContaDialogComponent,
        ContaPopupComponent,
        ContaDeleteDialogComponent,
        ContaDeletePopupComponent,
    ],
    providers: [
        ContaService,
        ContaPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EsJhipsterContaModule {}
