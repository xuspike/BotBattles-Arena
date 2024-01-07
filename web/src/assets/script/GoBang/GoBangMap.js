import { AcGameObject } from "../AcGameObject";
import { Player } from "./Player";

export class GoBangMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0; // 格子单位长度

        this.rows = 17;
        this.cols = 17;
        this.points = [
            {
                x: 4,
                y: 4
            },
            {
                x: this.rows - 4,
                y: 4
            },
            {
                x: 4,
                y: this.cols - 4
            },
            {
                x: this.rows - 4,
                y: this.cols - 4
            },
        ];

       
        this.map = new Array();
        this.PlayerA = new Player({id: 0, color: "black"}, this);
        this.PlayerB = new Player({id: 1, color: "white"}, this);
    }

    start() {
        this.init_map();
        this.add_listening_events();
    }

    init_map() {
        for(let i = 0; i < 17; i ++) {
            this.map[i] = new Array();
            for(let j = 0; j < 17; j ++)
                this.map[i][j] = 0;
        }
            
    }

    add_listening_events() {
        this.ctx.canvas.addEventListener('click', e => {
            const rect = this.ctx.canvas.getBoundingClientRect();
            let x = (e.clientX - rect.left) / this.L, y = (e.clientY - rect.top) / this.L;
            let int_x = parseInt(x), int_y = parseInt(y);

            if(x - int_x <= 0.5) x = int_x;
            else x = int_x + 1;

            if(y - int_y <= 0.5) y = int_y;
            else y = int_y + 1;

            if(x >= 1 && x <= 16 && y >= 1 && y <= 16 && !this.map[x][y]) {
                // 先默认画黑色
                this.PlayerA.push_chess(x, y, "black");
                this.map[x][y] = 1;
            }
            

            console.log(x);
            console.log(y);
        });
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
        for(let r = 0; r < this.rows; r++) {
            for(let c = 0; c < this.cols; c++) {
                this.ctx.fillStyle = "rgb(153,102,51)";
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
                if(r >= 1 && c >= 1 && r < this.rows - 1 && c < this.cols - 1) {
                    this.ctx.strokeStyle = "black";
                    this.ctx.strokeRect(c * this.L, r * this.L, this.L, this.L);
                }
            }
        }
        for(let i = 0; i < this.points.length ; i++) {
            this.ctx.fillStyle = "black";
            this.ctx.beginPath();
            this.ctx.arc(this.points[i].x * this.L, this.points[i].y * this.L, 0.1 * this.L, 0, Math.PI * 2);
            this.ctx.fill();
        }
    }
}