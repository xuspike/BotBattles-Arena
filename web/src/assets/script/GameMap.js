import { AcGameObect } from "./AcGameObejct";
import { Wall } from "./Wall";
import { Snake } from './Snake';

export class GameMap extends AcGameObect {
    constructor(ctx, parent) {
        super();

        this.ctx = ctx;
        this.parent = parent;
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

    // 判断随机生成的地图中的两个蛇是否连通(DFS)
    check_connectivity(g, sx, sy, tx, ty) {
        if(sx === tx && sy === ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1 ,0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i ++ ){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) 
                return true;
        }
        return false;
    }

    // 创建所有的墙
    create_walls() {
        // 初始化二维bool数组
        const g = [];
        for(let r = 0; r < this.rows; r ++) {
            g[r] = [];
            for(let c = 0; c < this.cols; c ++) {
                g[r][c] = false;
            }
        }

        //给左右两边加上墙
        for(let r = 0; r < this.rows; r ++)
            g[r][0] = g[r][this.cols - 1] = true;

        // 给上下两边加上墙
        for(let c = 0; c < this.cols; c ++)
            g[0][c] = g[this.rows - 1][c] = true;

        // 创建随机障碍物
        for(let i = 0; i < this.inner_walls_count / 2; i ++) {
            // 每个障碍物枚举一千次，一般都会找到
            for(let j = 0; j < 1000; j ++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
                // 左下角和右上角不能有障碍物
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

                // 关于中心对称
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;
            }
        }

        // 深拷贝g数组，先将g数组JSON，然后解析出来
        const copy_g = JSON.parse(JSON.stringify(g));
        if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false;

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

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if(e.key === 'w') snake0.set_direction(0);
            else if(e.key === 'd') snake0.set_direction(1);
            else if(e.key === 's') snake0.set_direction(2);
            else if(e.key === 'a') snake0.set_direction(3);
            else if(e.key === 'ArrowUp') snake1.set_direction(0);
            else if(e.key === "ArrowRight") snake1.set_direction(1);
            else if(e.key === 'ArrowDown') snake1.set_direction(2);
            else if(e.key === 'ArrowLeft') snake1.set_direction(3);
        });
    }
    
    start() {
        // 直到生成合适的地图
        for(let i = 0; i < 1000; i ++) {
            if(this.create_walls()) break;
        }
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