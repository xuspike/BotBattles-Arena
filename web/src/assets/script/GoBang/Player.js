import { AcGameObject } from "../AcGameObject";
import { Chess } from "./Chess";

export class Player extends AcGameObject {
    constructor(info, gamemap) {
        super();
        this.gamemap = gamemap;
        this.id = info.id;
        this.color = info.color;
        this.chesses = [];

        this.last_L = this.gamemap.L;

        this.dx = [0, 1, 1, 1, 0, -1, -1, -1];
        this.dy = [-1, -1, 0, 1, 1, 1, 0, -1];
    }
    
    start() {
    }

    update() {
        this.render();
    }

    push_chess(step) {
        let flag = (step % 16 == 0) ? 0 : 1;
        let y = parseInt(step / 16 + flag);
        let x = parseInt(step % 16 + (1 - flag) * 16);
        if(this.id == 0) this.gamemap.map[x][y] = 0;
        else this.gamemap.map[x][y] = 1;
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
        if(!this.gamemap.store.state.record.is_record) {
            let count = 5;
            // 如果游戏结束，将连续的五子标识
            if(this.id == 0 && this.gamemap.store.state.pk.loser === 'B' && winner_direction != -1) {
                const chess = this.chesses[this.chesses.length - 1];
                let x = chess.r, y = chess.c;
                let i = 0;
                while(i < 5 && count > 0) {
                    if(this.last_L <= this.gamemap.L * 1.25) this.last_L += this.gamemap.L * 0.02;
                    if(this.gamemap.map[x][y] != 0) break;
                    this.gamemap.ctx.beginPath();
                    this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
                    let tx = x - 0.2;
                    let ty = y - 0.2;
                    
                    let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
                    style.addColorStop(0, "#ccc");
                    style.addColorStop(1, "#000");
                    this.gamemap.ctx.fillStyle = style;
                    this.gamemap.ctx.fill();
                    i ++;
                    count --;
                    x += this.dx[winner_direction], y += this.dy[winner_direction];
                }

                i = 0;
                x = chess.r, y = chess.c;

                while(i < 5 && count > 0) {
                    x += this.dx[winner_direction + 4], y += this.dy[winner_direction + 4];
                    if(this.gamemap.map[x][y] != 0) break;
                    i ++;
                    count --;
                    if(this.last_L <= this.gamemap.L * 1.25) this.last_L += this.gamemap.L * 0.02;
                    this.gamemap.ctx.beginPath();
                    this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
                    let tx = x - 0.2;
                    let ty = y - 0.2;
                    
                    let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
                    style.addColorStop(0, "#ccc");
                    style.addColorStop(1, "#000");
                    this.gamemap.ctx.fillStyle = style;
                    this.gamemap.ctx.fill();
                }
                // for(let i = 0; i < 5; i ++) {
                    // this.gamemap.ctx.beginPath();
                    // this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, chess.current_L * 0.47, 0, Math.PI * 2);
                    // this.gamemap.ctx.lineWidth = chess.current_L * 0.05;
                    // this.gamemap.ctx.strokeStyle = 'red';
                    // this.gamemap.ctx.stroke();
                // }
            } else if(this.id == 1 && this.gamemap.store.state.pk.loser === 'A' && winner_direction != -1) {
                const chess = this.chesses[this.chesses.length - 1];
                let x = chess.r, y = chess.c;

                let i = 0;
                while(i < 5 && count > 0) {
                    if(this.gamemap.map[x][y] != 1) break;
                    if(this.last_L <= this.gamemap.L * 1.25) this.last_L += this.gamemap.L * 0.02;
                    this.gamemap.ctx.beginPath();
                    this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
                    let tx = x - 0.2;
                    let ty = y - 0.2;
                    
                    let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
                    style.addColorStop(0, "#666");
                    style.addColorStop(1, "#fff");
                    this.gamemap.ctx.fillStyle = style;
                    this.gamemap.ctx.fill();
                    i ++;
                    count --;
                    x += this.dx[winner_direction], y += this.dy[winner_direction];
                }

                i = 0;
                x = chess.r, y = chess.c;

                while(i < 5 && count > 0) {
                    x += this.dx[winner_direction + 4], y += this.dy[winner_direction + 4];
                    if(this.gamemap.map[x][y] != 1) break;
                    if(this.last_L <= this.gamemap.L * 1.25) this.last_L += this.gamemap.L * 0.02;
                    this.gamemap.ctx.beginPath();
                    this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
                    let tx = x - 0.2;
                    let ty = y - 0.2;
                    
                    let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
                    style.addColorStop(0, "#666");
                    style.addColorStop(1, "#fff");
                    this.gamemap.ctx.fillStyle = style;
                    this.gamemap.ctx.fill();
                    i ++;
                    count --;
                }

                // for(let i = 0; i < 5; i ++) {
                //     x += this.dx[winner_direction], y += this.dy[winner_direction];
                //     if(this.last_L <= this.gamemap.L * 1.2)
                //     this.last_L += this.gamemap.L * 0.02;
                //     this.gamemap.ctx.beginPath();
                //     this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
                //     let tx = x + 0.2;
                //     let ty = y + 0.2;
                //     let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
                //     style.addColorStop(0, "#666");
                //     style.addColorStop(1, "#fff");
                //     this.gamemap.ctx.fillStyle = style;
                //     this.gamemap.ctx.fill();
                // }
            }
        }
        
        // } else {
        //     console.log(this.gamemap.store.state.record);
        //     const winner_direction = this.gamemap.store.state.record.winner_direction;
        //     console.log(winner_direction);
        //     if(this.id == 0 && this.gamemap.store.state.record.record_loser === 'B' && winner_direction != -1) {
        //         const chess = this.chesses[this.chesses.length - 1];
        //         let x = chess.r - this.dx[winner_direction], y = chess.c - this.dy[winner_direction];
        //         for(let i = 0; i < 5; i ++) {
        //             x += this.dx[winner_direction], y += this.dy[winner_direction];
        //             if(this.last_L <= this.gamemap.L * 1.25) this.last_L += this.gamemap.L * 0.02;
        //             this.gamemap.ctx.beginPath();
        //             this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
        //             let tx = x - 0.2;
        //             let ty = y - 0.2;
                    
        //             let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
        //             style.addColorStop(0, "#ccc");
        //             style.addColorStop(1, "#000");
        //             this.gamemap.ctx.fillStyle = style;
        //             this.gamemap.ctx.fill();
        //             // this.gamemap.ctx.beginPath();
        //             // this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, chess.current_L * 0.47, 0, Math.PI * 2);
        //             // this.gamemap.ctx.lineWidth = chess.current_L * 0.05;
        //             // this.gamemap.ctx.strokeStyle = 'red';
        //             // this.gamemap.ctx.stroke();
        //         }
        //     } else if(this.id == 1 && this.gamemap.store.state.record.record_loser === 'A' && winner_direction != -1) {
        //         const chess = this.chesses[this.chesses.length - 1];
        //         console.log(this.chesses.length - 1);
        //         console.log(chess);
        //         let x = chess.r - this.dx[winner_direction], y = chess.c - this.dy[winner_direction];
        //         for(let i = 0; i < 5; i ++) {
        //             x += this.dx[winner_direction], y += this.dy[winner_direction];
        //             if(this.last_L <= this.gamemap.L * 1.2)
        //             this.last_L += this.gamemap.L * 0.02;
        //             this.gamemap.ctx.beginPath();
        //             this.gamemap.ctx.arc(x * this.gamemap.L, y * this.gamemap.L, this.last_L * 0.47, 0, Math.PI * 2);
        //             let tx = x + 0.2;
        //             let ty = y + 0.2;
        //             let style = this.gamemap.ctx.createRadialGradient(tx * this.gamemap.L, ty * this.gamemap.L, 0 * this.gamemap.L, tx * this.gamemap.L, ty * this.gamemap.L, this.last_L * 0.5);
        //             style.addColorStop(0, "#666");
        //             style.addColorStop(1, "#fff");
        //             this.gamemap.ctx.fillStyle = style;
        //             this.gamemap.ctx.fill();
        //     }
        // }
        // }
        
    }
}