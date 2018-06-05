import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Conta } from './conta.model';
import { ContaPopupService } from './conta-popup.service';
import { ContaService } from './conta.service';
import { User, UserService } from '../../shared';

@Component({
    selector: 'jhi-conta-dialog',
    templateUrl: './conta-dialog.component.html'
})
export class ContaDialogComponent implements OnInit {

    conta: Conta;
    isSaving: boolean;

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private contaService: ContaService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: HttpResponse<User[]>) => { this.users = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.conta.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contaService.update(this.conta));
        } else {
            this.subscribeToSaveResponse(
                this.contaService.create(this.conta));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Conta>>) {
        result.subscribe((res: HttpResponse<Conta>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Conta) {
        this.eventManager.broadcast({ name: 'contaListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-conta-popup',
    template: ''
})
export class ContaPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contaPopupService: ContaPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contaPopupService
                    .open(ContaDialogComponent as Component, params['id']);
            } else {
                this.contaPopupService
                    .open(ContaDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
