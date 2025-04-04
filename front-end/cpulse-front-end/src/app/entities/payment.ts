export class Payment {
    constructor(
        public id?: number,
        public cardNumber?: number,
        public expiryMonth?: number,
        public expiryYear?: number,
        public cvv?: number,
        public amount?: number,
        public userId?: number,
        public paymentDate?: Date,
        public userEmail?: string,
        public billingAddress? : string,
        public cardHolderName? : string,
        public zipCode? : number,
        public country? : string
    ){}
}
