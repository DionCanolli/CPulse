export class User {
    constructor (
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public username?: string,
        public email?: string,
        public phoneNumber?: string,
        public password?: string,
        public roleId? : number
    ){}
}
