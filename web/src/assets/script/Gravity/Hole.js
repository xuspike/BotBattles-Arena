import { AcGameObject } from "../AcGameObject";

export class Hole extends AcGameObject {
    constructor(gamemap) {
        super();

        this.gamemap = gamemap;

        this.rows = 7;
        this.cols = 7;

    }

    start() {
    }

    update() {
        this.render();
    }

    render() {
        // 绘制洞
        this.gamemap.ctx.fillStyle = '#555555';
        for(let c = 0; c < this.cols; c ++)
            for(let r = 0; r < this.rows; r ++) {
                // 绘制深灰色圆
                this.gamemap.ctx.beginPath();
                this.gamemap.ctx.arc((c + 0.5) * this.gamemap.L, (r + 0.5) * this.gamemap.L, this.gamemap.L * 0.35, 0, 2 * Math.PI);
                this.gamemap.ctx.closePath();
                this.gamemap.ctx.fill();
            }
        // this.ctx.fillStyle = "skyblue";
        // this.ctx.fillRect(0, 0, this.L * 7, this.L * 8);
    }
}