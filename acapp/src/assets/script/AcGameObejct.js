const AC_GAME_OBJECTS = [];

export class AcGameObect {
    constructor() {
        AC_GAME_OBJECTS.push(this); // 在constructor构造数组中，将创建的对象存入数组中。
        this.timedelta = 0; // this.timedelta是用来存储两帧之间的时间间隔，后续会通过该时间来控制蛇的移动距离。
        this.has_called_start = false; // this.has_called_start用来标记当前是否是渲染第一帧。
    }

    start() { // 第一帧调用

    }

    update() { // 除了第一帧都会调用

    }

    on_destroy() { // 删除对象前调用

    }

    destroy() { // 删除对象
        this.on_destroy();

        for(let i in AC_GAME_OBJECTS) {
            const obj = AC_GAME_OBJECTS[i];
            if(obj === this) {
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}

// 上一帧的时间
let last_timestamp;
const step = (timestamp) => {
    for(let obj of AC_GAME_OBJECTS) {
        if(!obj.has_called_start) {
            obj.has_called_start = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);