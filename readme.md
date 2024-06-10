# BotBattles-Arena
[页面地址](https://xrookie.xyz/pk)

## 项目介绍
本项目是基于 SpringBoot+Vue3 实现的贪吃蛇、五子棋和重力四子棋游戏的编程对战平台。游戏画面均由 Canvas 绘制，并自主完成游戏引擎实现游戏画面的动态渲染。通过 WebSocket 实现了游戏联机对战和好友聊天功能，且在联机匹配过程中，使用了多线程技术实现数据同步，并随着时间扩大段位分匹配域，从而有效减少玩家匹配时间。在代码执行过程中，通过在 Java 执行 Shell 命令实现了代码执行服务，并采用生产者-消费者模型，提高了代码执行效率。在游戏对局结束后，通过记录游戏对局信息，实现了游戏回放。 

## 游戏介绍
在游戏对战前，玩家可以选择自己提前创建的bot代码进行游戏匹配，匹配成功后游戏对局开始。目前只支持编程语言为java的bot代码的执行，并且在创造Bot时暂时未实现调试功能。下面只介绍玩家操控时的游戏方式。

### 贪吃蛇
玩家可通过wsad来操控蛇的移动方向，并躲避障碍物以及对手蛇身，蛇身会随着步数而增长，直到其中一方获胜或平局，则游戏结束。
下面是普通的贪吃蛇bot代码：
```
package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer> {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i ++ ) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0, k = 0; i < 13; i ++ ) {
            for (int j = 0; j < 14; j ++, k ++ ) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }

        System.out.println(strs);

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);

        for (Cell c: aCells) g[c.x][c.y] = 1;
        for (Cell c: bCells) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i ++ ) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }

        return 0;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 五子棋
玩家可以在棋盘上点击鼠标左键将棋子下在相应为止。目前暂未完成五子棋bot代码。

### 重力四子棋
玩家可以在棋盘上点击相应的列，棋子会从上而下落在相应的列上。
下面是普通的重力四子棋bot代码：
```
package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer>{
    public int [][]g;

    //初始化g
    public void init_g() {
        g = new int[7][7];
        for(int i = 0; i < 7; i ++)
            for(int j = 0; j < 7; j ++)
                g[i][j] = -1;
    }

    public int get_int(String step) {
        int res = 0;
        for(int i = 0; i < step.length(); i ++)
            res = res * 10 + (step.charAt(i) - '0');

        return res;
    }

    public int change_step(int x, int y) {
        return y * 7 + x + 1;
    }

    // 获取下一步
    public Integer nextDrop(String input) {
        int[] dx = {0, 1, 1, 1, 0, -1, -1, -1}, dy = {-1, -1, 0, 1, 1, 1, 0, -1};
        String[] strs = input.split("#");
        strs[0] = strs[0].substring(1, strs[0].length() - 1);
        strs[1] = strs[1].substring(1, strs[1].length() - 1);
        String[] a_steps = new String[0], b_steps = new String[0];

        if(strs[0].length() > 0) a_steps = strs[0].split(",");
        if(strs[1].length() > 0) b_steps = strs[1].split(",");

        for(int i = 0; i < a_steps.length; i ++) {
            String str_step = a_steps[i];
            if("".equals(str_step)) continue;
            int step = get_int(str_step);
            int flag = (step % 7 == 0) ? -1 : 0;
            int y = step / 7 + flag, x = (step - 1) % 7;
            g[x][y] = 0;
        }

        for(int i = 0; i < b_steps.length; i ++) {
            String str_step = b_steps[i];
            if("".equals(str_step)) continue;
            int step = get_int(str_step);
            int flag = (step % 7 == 0) ? -1 : 0;
            int y = step / 7 + flag, x = (step - 1) % 7;
            g[x][y] = 1;
        }
        for(int y = 6; y >= 0; y --)
            for(int x = 6; x >= 0; x --) {
                if(g[x][y] == -1) {
                    return change_step(x, y);
                }
            }
        return -1;
    }

    @Override
    public Integer get() {
        init_g();
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextDrop(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
```
