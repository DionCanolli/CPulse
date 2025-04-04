import { Product } from "./product";

export class PaginatedProducts {
    constructor(
        public allProducts?: Product[],
        public totalProducts?: number
    ){}
}
