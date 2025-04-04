export class Product {
    constructor(
        public productId?: number,
        public productName?: string,
        public productDescription?: string,
        public productCategory?: string,
        public productImageUrl?: string,
        public productPrice?: number,
        public productStockQuantity?: number
    ){}
}
