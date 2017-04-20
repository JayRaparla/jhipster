import { Transaction } from '../transaction';
export class Remark {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public date?: any,
        public transaction?: Transaction,
    ) {
    }
}
