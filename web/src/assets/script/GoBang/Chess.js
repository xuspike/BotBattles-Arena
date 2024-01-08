export class Chess {
    constructor(r, c, color) {
        this.c = c;
        this.r = r;
        this.color = color;
        this.current_L = 0;
        this.is_last = true;

        this.status = "enlarging"; // "enlarging表示棋子在变大，stop表示棋子停止增大"
        this.eps = 1e-2; // 允许的误差
    }
}