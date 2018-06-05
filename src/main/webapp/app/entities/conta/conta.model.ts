import { BaseEntity, User } from './../../shared';

export class Conta implements BaseEntity {
    constructor(
        public id?: number,
        public conta?: string,
        public valor?: number,
        public diaVencimento?: number,
        public numParcelas?: number,
        public user?: User,
    ) {
    }
}
