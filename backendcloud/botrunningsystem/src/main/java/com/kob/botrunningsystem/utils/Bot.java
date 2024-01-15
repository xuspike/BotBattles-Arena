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
