import { AcGameObject } from "../AcGameObject";
import { Chess } from "./Chess";

export class Player extends AcGameObject {
    constructor(info, gamemap) {
        super();
        this.gamemap = gamemap;
        this.id = info.id;
        this.color = info.color;
        this.chesses = [];

        this.dx = [-1, -1, 0, 1, 1, 1, 0, -1];
        this.dy = [0, 1, 1, 1, 0, -1, -1, -1];
    }
    
    start() {
    }

    update() {
        this.render();
    }

    push_chess(step) {
        let x = parseInt(step / 16 + 1);
        let y = parseInt(step % 16);
        this.chesses.push(new Chess(x, y, this.color));
    }

    render() {
        for(let i = 0; i < this.chesses.length; i ++) {
            const chess = this.chesses[i];
            if(chess.status === "enlarging") {
                if(chess.current_L < this.gamemap.L) {
                    chess.current_L += this.gamemap.L * 0.05;
                } else {
                    chess.current_L = this.gamemap.L;
                    chess.status = "stop"
                }
            }
            this.gamemap.ctx.beginPath();
            this.gamemap.ctx.arc(chess.r * this.gamemap.L, chess.c * this.gamemap.L, chess.current_L * 0.47, 0, Math.PI * 2);

            // 让棋子有立体感
            let tx = this.color === "black" ? chess.r - 0.2 : chess.r + 0.2;
            let ty = this.color === "black" ? chess.c - 0.2 : chess.c + 0.2;
            let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, chess.current_L * 0.5);
            style.addColorStop(0, this.color === "black" ? '#ccc' : '#666');
            style.addColorStop(1, this.color === "black" ? '#000' : '#fff');
            this.gamemap.ctx.fillStyle = style;

            // 添加阴影
            this.gamemap.ctx.shadowBlur = 4;
            this.gamemap.ctx.shadowColor = '#333';
            this.gamemap.ctx.shadowOffsetX = 4;
            this.gamemap.ctx.shadowOffsetY = 4;
            this.gamemap.ctx.fill();
            this.gamemap.ctx.closePath();

            if(i == this.chesses.length - 1 && chess.is_last) {
                //将当前所下棋子标识
                this.gamemap.ctx.beginPath();
                this.gamemap.ctx.arc(chess.r * this.gamemap.L, chess.c * this.gamemap.L, chess.current_L * 0.15, 0, Math.PI * 2);
                this.gamemap.ctx.fillStyle = "red";
                this.gamemap.ctx.fill();
                this.gamemap.ctx.closePath();
                // this.gamemap.ctx.arc(chess.r * this.gamemap.L, chess.c * this.gamemap.L, chess.current_L * 0.3, 0, Math.PI * 2);
                // this.gamemap.ctx.lineWidth = chess.current_L * 0.1;
                // this.gamemap.ctx.strokeStyle = '#999999';
                // this.gamemap.ctx.stroke();
            }
        }

        const winner_direction = this.gamemap.store.state.pk.winner_direction;

        // 如果游戏结束，将连续的五子标识
        if(this.id == 0 && this.gamemap.store.state.pk.loser === 'B') {
            const chess = this.chesses[this.chesses.length - 1];
            let x = chess.r - this.dx[winner_direction], y = chess.c - this.dy[winner_direction];
            for(let i = 0; i < 5; i ++) {
                x += this.dx[winner_direction], y += this.dy[winner_direction];
                this.gamemap.ctx.beginPath();
                this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, chess.current_L * 0.47, 0, Math.PI * 2);
                this.gamemap.ctx.lineWidth = chess.current_L * 0.05;
                this.gamemap.ctx.strokeStyle = 'red';
                this.gamemap.ctx.stroke();
                // this.gamemap.ctx.beginPath();
                // this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, chess.current_L * 0.47, 0, Math.PI * 2);
                // let style = this.gamemap.ctx.createRadialGradient(x * this.gamemap.L, y * this.gamemap.L, 0 * this.gamemap.L, x * this.gamemap.L, y * this.gamemap.L, this.gamemap.L * 0.5);
                // style.addColorStop(0, 'red');
                // this.gamemap.ctx.fillStyle = style;
                // this.gamemap.ctx.fill();
            }
        }
    }
}