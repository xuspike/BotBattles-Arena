export class Chess {
    constructor(c, r, color) {
        this.c = c;
        this.r = r;
        this.pre_c = c;
        this.pre_r = 0;
        this.status = "move"; // idle表示静止，move表示移动
        this.color = color;

        this.is_last = true;
    }
}