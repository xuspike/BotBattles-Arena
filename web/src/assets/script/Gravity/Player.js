import { AcGameObject } from "../AcGameObject";
import { Chess } from "./Chess";

export class Player extends AcGameObject {
    constructor(info, gamemap) {
        super();
        this.gamemap = gamemap;
        this.id = info.id;
        this.color = info.color;
        this.speed = 14; // 棋子落下速度为每秒十四格
        this.chesses = [];

        this.dx = [0, 1, 1, 1, 0, -1, -1, -1];
        this.dy = [-1, -1, 0, 1, 1, 1, 0, -1];
    }

    start() {

    }

    update() {
        this.render();
    }

    fall_chess(step) {
        let flag = (step % 7 == 0) ? -1 : 0;
        let y = parseInt(step / 7 + flag);
        let x = parseInt(step - 1) % 7;
        console.log("fall_chess, ", x, " ", y);
        if(this.id == 0) this.gamemap.map[x][y] = 0, console.log("in_map");
        else this.gamemap.map[x][y] = 1;
        this.chesses.push(new Chess(x, y, this.color));
    }

    render() {
        for(let i = 0; i < this.chesses.length; i ++) {
            const chess = this.chesses[i];
            if(chess.status === "move") {
                let move_distance = this.speed * this.timedelta / 1000;
                console.log("move_distance = ", move_distance);
                chess.pre_r += move_distance;
                if(chess.pre_r > chess.r) {
                    chess.pre_r = chess.r;
                    chess.status = "idle";
                } 
            } 
            this.gamemap.ctx.fillStyle = this.color;
            this.gamemap.ctx.beginPath();
            this.gamemap.ctx.arc((chess.pre_c + 0.5)* this.gamemap.L, (chess.pre_r + 0.5) * this.gamemap.L, this.gamemap.L * 0.35, 0, 2 * Math.PI);
            this.gamemap.ctx.closePath();
            this.gamemap.ctx.fill();
        }
    }
}