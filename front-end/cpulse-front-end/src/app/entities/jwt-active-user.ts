export class JwtActiveUser {
    constructor(
        public theId?: number,
        public userId?: number,
        public jwtToken?: string
    ){}
}
