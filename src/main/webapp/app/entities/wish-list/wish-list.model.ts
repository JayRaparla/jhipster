import { User } from '../../shared';
export class WishList {
    constructor(
        public id?: number,
        public nameOfList?: string,
        public user?: User,
    ) {
    }
}
