import { User } from '../../shared';
import { Tag } from '../tag';
import { Remark } from '../remark';
export class Transaction {
    constructor(
        public id?: number,
        public date?: string,
        public description?: string,
        public amount?: string,
        public user?: User,
        public tag?: Tag,
        public remark?: Remark,
    ) {
    }
}
