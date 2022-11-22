export class Cell {
    constructor(r, c) {
        this.c = c;
        this.r = r;
        // 蛇身格子中心点
        this.x = c + 0.5;
        this.y = r + 0.5;
        
    }
}