import { AcGameObject } from "../AcGameObject";
import { Player } from "./Player";
import { Hole } from "./Hole";
import {Chess} from "./Chess";

export class GravityMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;

        this.L = 0;
        this.rows = 7;
        this.cols = 7;

        this.collision_music = new Audio(require("@/assets/sound/棋子碰撞音效.wav"));

        this.map = new Array();
        this.hole = new Hole(this);
        this.PlayerA = new Player({id: 0, color: "#FE490A"}, this);
        this.PlayerB = new Player({id: 1, color: "#FFE746"}, this);
    }

    start() {
        this.init_map();
        this.add_listening_events();
    }

    init_map() {
        for(let i = 0; i < 7; i ++) {
            this.map[i] = new Array();
            for(let j = 0; j < 7; j ++) {
                this.map[i][j] = -1;
            }
        }
        // 解決報錯
        this.collision_music.currentTime = 0;
    }

    get_aidY(x) {
        for(let i = 6; i >= 0; i --) {
            if(this.map[x][i] == -1) return i;
        }
        return -1;
    }

    add_listening_events() {
        if(this.store.state.record.is_record) {
            const a_steps = this.store.state.record.a_steps.split(',');
            const b_steps = this.store.state.record.b_steps.split(',');
            if(a_steps[0] === '') a_steps.pop(); 
            if(b_steps[0] === '') b_steps.pop();

            let i = 0, j = 0;
            let x = -1, y = -1;
            const interval_id = setInterval(() => {
                // 黑子下
                if(i <= j && i < a_steps.length) {
                    let a_step = parseInt(a_steps[i]);
                    let flag = (a_step % 7 == 0) ? -1 : 0;
                    y = parseInt(a_step / 7 + flag), x = parseInt(a_step - 1) % 7;
                    this.PlayerA.chesses.push(new Chess(x, y, "#FE490A"));
                    if(j >= 1) this.PlayerB.chesses[j - 1].is_last = false;
                    i ++;
                } else if(i > j && j < b_steps.length) { // 白子下
                    let b_step = parseInt(b_steps[j]);
                    let flag = (b_step % 7 == 0) ? -1 : 0;
                    y = parseInt(b_step / 7 + flag), x = parseInt(b_step - 1) % 7;
                    this.PlayerB.chesses.push(new Chess(x, y, "#FFE746"));
                    this.PlayerA.chesses[i - 1].is_last = false;
                    j ++;
                }
                if(i == a_steps.length && j == b_steps.length)
                    clearInterval(interval_id);
            }, 500)
        } else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener('click', e => { 
                const rect = this.ctx.canvas.getBoundingClientRect();
                let x = (e.clientX - rect.left) / this.L, y = (e.clientY - rect.top) / this.L;
                x = parseInt(x), y = parseInt(y);

                // 前端也可以先判断是否合法
                if(x >= 0 && x <= 6 && y >= 0 && y <= 6) {
                    let aid_x = x, aid_y = this.get_aidY(x);
                    if(aid_y != -1) {
                        let step = aid_y * 7 + aid_x + 1;
                        if(this.store.state.user.id == this.store.state.pk.a_id) {
                            this.store.state.pk.socket.send(JSON.stringify({
                                event: "fall",
                                step: step,
                            }))
                        } else if(this.store.state.user.id == this.store.state.pk.b_id) {
                            this.store.state.pk.socket.send(JSON.stringify({
                                event: "fall",
                                step: step,
                            }))
                        }
                    }
                }
            });
        }
    }

    update_size() {
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
    }

    render() {
        // for(let c = 0; c < 7; c ++) {
        //     if(c % 2 == 0) this.ctx.fillStyle = 'rgba(173, 216, 230, 0.5)';
        //     else this.ctx.fillStyle = "rgba(255, 255, 240, 0.5)";
        //     for(let r = 0; r < 7; r ++) {
        //         this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
        //     }
        // }

        const size = this.L * 7;
        const x = 0, y = 0;
        const borderRadius = 10;

        // 绘制棋盘
        this.ctx.beginPath();
        this.ctx.moveTo(x + borderRadius, y);
        this.ctx.arcTo(x + size, y, x + size, y + borderRadius, borderRadius);
        this.ctx.arcTo(x + size, y + size, x + size - borderRadius, y + size, borderRadius);
        this.ctx.arcTo(x, y + size, x, y + size - borderRadius, borderRadius);
        this.ctx.arcTo(x, y, x + borderRadius, y, borderRadius);
        this.ctx.closePath();
        this.ctx.fillStyle = '#59A9C7';
        this.ctx.fill();
    }
}