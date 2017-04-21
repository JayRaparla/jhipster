import { WishList } from '../wish-list';
import { User } from '../../shared';
export class Wish {
    constructor(
        public id?: number,
        public description?: string,
        public addedOn?: any,
        public completeBy?: any,
        public wishList?: WishList,
        public user?: User,
    ) {
    }
}
