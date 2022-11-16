import { AcGameObect } from "./AcGameObejct";
import { Cell } from "./Cell";

export class Snake extends AcGameObect {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // 存放蛇的身体，cells[0]存放蛇头
        this.next_cell = null; // 下一步的目标位置

        this.speed = 5; // 蛇每秒走5个格子 
        this.direction = -1; // -1表示没有指令，0、1、2、3表示上右下左
        this.status = "idle"; // idle表示静止，move表示移动，die表示死亡

        this.dr = [-1, 0, 1, 0]; // 4个方向行的偏移量
        this.dc = [0, 1, 0, -1]; // 4个方向列的偏移量

        this.step = 0;  // 游戏回合数
        this.eps = 1e-2; // 允许的误差

        // 初始状态蛇眼睛的方向
        this.eye_direction = 0;
        if(this.id === 1) this.eye_direction = 2;

        this.eye_dx = [ // 蛇眼睛不同方向的x相对于圆中心点的偏移量
            [-1, 1], // 两只眼睛
            [1, 1],
            [1, -1],
            [-1, -1],
        ];
        this.eye_dy = [ // 蛇眼睛不同方向的y相对于圆中心点的偏移量
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]
    }

    start() {

    }

    set_direction(d) { // 由于bot后续移动方向由后端提供，因此提供了一个统一的接口
        this.direction = d;
    }

    check_tail_increasing() { // 检测当前回合，蛇的长度会否增加
        if(this.step <= 10) return true;
        if(this.step % 3 === 1) return true;
        return false;
    }

    next_step() { // 将蛇的状态变为移动
        const d = this.direction;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1; // 清空操作
        this.status = "move";
        this.step ++;

        const k = this.cells.length;
        // 添加一个新头
        for(let i = k; i > 0; i --) {
            // 不能写成this.cells[i] = this.cells[i - 1]，这是浅拷贝写法。
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < this.eps) {
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.status = "idle"; // 走完了，停下来

            if(!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta / 1000; // 每两帧之间移动的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if(!this.check_tail_increasing()) { // 若蛇身不增长
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                // 由于蛇尾移动距离和蛇头移动距离相同，move_distance和distance不用重新计算
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update() {
        if(this.status === "move") {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if(this.status === "die") {
            ctx.fillStyle = "white";
        }

        for(const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2);
            ctx.fill();
        }

        // 用矩形补充蛇身
        for(let i = 1; i < this.cells.length; i ++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if(Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            if(Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.5 + 0.1) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5 + 0.1) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        ctx.fillStyle = "black";
        for(let i = 0; i < 2; i ++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}