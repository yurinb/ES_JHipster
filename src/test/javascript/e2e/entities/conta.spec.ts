import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Conta e2e test', () => {

    let navBarPage: NavBarPage;
    let contaDialogPage: ContaDialogPage;
    let contaComponentsPage: ContaComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Contas', () => {
        navBarPage.goToEntity('conta');
        contaComponentsPage = new ContaComponentsPage();
        expect(contaComponentsPage.getTitle())
            .toMatch(/esJhipsterApp.conta.home.title/);

    });

    it('should load create Conta dialog', () => {
        contaComponentsPage.clickOnCreateButton();
        contaDialogPage = new ContaDialogPage();
        expect(contaDialogPage.getModalTitle())
            .toMatch(/esJhipsterApp.conta.home.createOrEditLabel/);
        contaDialogPage.close();
    });

    it('should create and save Contas', () => {
        contaComponentsPage.clickOnCreateButton();
        contaDialogPage.setContaInput('conta');
        expect(contaDialogPage.getContaInput()).toMatch('conta');
        contaDialogPage.setValorInput('5');
        expect(contaDialogPage.getValorInput()).toMatch('5');
        contaDialogPage.setDiaVencimentoInput('5');
        expect(contaDialogPage.getDiaVencimentoInput()).toMatch('5');
        contaDialogPage.setNumParcelasInput('5');
        expect(contaDialogPage.getNumParcelasInput()).toMatch('5');
        contaDialogPage.userSelectLastOption();
        contaDialogPage.save();
        expect(contaDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ContaComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-conta div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ContaDialogPage {
    modalTitle = element(by.css('h4#myContaLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    contaInput = element(by.css('input#field_conta'));
    valorInput = element(by.css('input#field_valor'));
    diaVencimentoInput = element(by.css('input#field_diaVencimento'));
    numParcelasInput = element(by.css('input#field_numParcelas'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setContaInput = function(conta) {
        this.contaInput.sendKeys(conta);
    };

    getContaInput = function() {
        return this.contaInput.getAttribute('value');
    };

    setValorInput = function(valor) {
        this.valorInput.sendKeys(valor);
    };

    getValorInput = function() {
        return this.valorInput.getAttribute('value');
    };

    setDiaVencimentoInput = function(diaVencimento) {
        this.diaVencimentoInput.sendKeys(diaVencimento);
    };

    getDiaVencimentoInput = function() {
        return this.diaVencimentoInput.getAttribute('value');
    };

    setNumParcelasInput = function(numParcelas) {
        this.numParcelasInput.sendKeys(numParcelas);
    };

    getNumParcelasInput = function() {
        return this.numParcelasInput.getAttribute('value');
    };

    userSelectLastOption = function() {
        this.userSelect.all(by.tagName('option')).last().click();
    };

    userSelectOption = function(option) {
        this.userSelect.sendKeys(option);
    };

    getUserSelect = function() {
        return this.userSelect;
    };

    getUserSelectedOption = function() {
        return this.userSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
