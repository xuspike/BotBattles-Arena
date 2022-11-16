import { AcGameObect } from "./AcGameObejct";
import { Wall } from "./Wall";
import { Snake } from './Snake';

export class GameMap extends AcGameObect {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0; // 格子单位长度

        // 行列的格子数
        this.rows = 13;
        this.cols = 14;

        // 里面的障碍物数量
        this.inner_walls_count = 20;
        this.walls = []; // 存储所有的墙

        // 创建两条蛇
        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];
    }

    // 创建所有的墙
    create_walls() {
        const g = this.store.state.pk.gamemap;

        for(let r = 0; r < this.rows; r ++) {
            for(let c = 0; c < this.cols; c ++) {
                if(g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        
        return true;
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if(e.key === 'w') d = 0;
            else if(e.key === 'd') d = 1;
            else if(e.key === 's') d = 2;
            else if(e.key === 'a') d = 3;

            if(d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }))
            }
        });
    }
    
    start() {
        this.create_walls();
        this.add_listening_events();
    }

    // 每一帧更新边长
    update_size() {
        // 浮点数会因为精度问题导致画的格子之间有空隙，因此需要转化为整型
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready() {  // 判断两条蛇是否都准备好下一个回合
        for(const snake of this.snakes) {
            if(snake.status !== "idle") return false; // 两条蛇不处于静止状态
            if(snake.direction === -1) return false; // 两条蛇没有移动方向
        }
        return true;
    }

    next_step() { // 让两条蛇进入下一回合
        for(const snake of this.snakes) {
            snake.next_step();
        }
    }

    check_valid(cell) { // 检测两蛇是否撞上障碍物
        for(const wall of this.walls) {
            if(wall.r === cell.r && wall.c === cell.c) {
                return false;
            }
        }
        for(const snake of this.snakes) {
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()) { // 当蛇尾会前进时，蛇尾不需要判断
                k --;
            }
            for(let i = 0; i < k; i ++) {
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }
        return true;
    }

    update() {
        this.update_size();
        if(this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    render() {
        // 偶数格和奇数格的颜色
        const color_even = "#AAD751", color_add = "#A2D149";
        for(let r = 0; r < this.rows; r ++)
            for(let c = 0; c < this.cols; c ++) {
                if((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_add;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }

    }
}