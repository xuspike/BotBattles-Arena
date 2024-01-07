export class Chess {
    constructor(r, c, color) {
        this.c = c;
        this.r = r;
        this.color = color;
        this.current_L = 0;

        this.status = "enlarging"; // "enlarging表示棋子在变大，stop表示棋子停止增大"
        this.eps = 1e-2; // 允许的误差
    }
}

// export class ChessesMap extends AcGameObject {
//     constructor(info, gamemap) {
//         super();

//         this.r = info.r;
//         this.c = info.c;
//         this.color = info.color;
//         this.gamemap = gamemap;
//         this.current_width = 0;

//         this.status = "enlarging"; // "enlarging表示棋子在变大，stop表示棋子停止增大"
//         this.eps = 1e-2; // 允许的误差
//     }

//     update() {
//         this.render();
//     }

//     render() {
//         const L = this.gamemap.L;
//         const ctx = this.gamemap.ctx;

//         ctx.fillStyle = this.color;
        
//         if(this.current_width < L / 2) {
//              this.current_width += L * 0.018;
//         } else {
//             this.current_width = L / 2;
//         }

//         ctx.arc(this.r * L, this.c * L, L * 0.36, 0, Math.PI * 2);
//     }
// }