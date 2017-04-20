import { Transaction } from '../transaction';
export class Tag {
    constructor(
        public id?: number,
        public name?: string,
        public transaction?: Transaction,
    ) {
    }
}
