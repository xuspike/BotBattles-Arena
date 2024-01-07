import { AcGameObject } from "../AcGameObject";
import { Chess } from "./Chess";

export class Player extends AcGameObject {
    constructor(info, gamemap) {
        super();
        console.log(info, gamemap);
        this.gamemap = gamemap;
        this.id = info.id;
        this.color = info.color;
        this.chesses = [];
    }
    
    start() {
    }

    update() {
        this.render();
    }

    push_chess(r, c, color) {
        this.chesses.push(new Chess(r, c, color));
    }

    render() {
        this.gamemap.ctx.fillStyle = this.color;
        for(let i = 0; i < this.chesses.length; i ++) {
            const chess = this.chesses[i];
            if(chess.status === "enlarging") {
                if(chess.current_L < this.gamemap.L) {
                    console.log(chess.current_L);
                    chess.current_L += this.gamemap.L * 0.05;
                } else {
                    chess.current_L = this.gamemap.L;
                    chess.status = "stop"
                }
            }
            this.gamemap.ctx.beginPath();
            this.gamemap.ctx.arc(chess.r * this.gamemap.L, chess.c * this.gamemap.L, chess.current_L * 0.5, 0, Math.PI * 2);
            this.gamemap.ctx.fill();
        }
    }
}