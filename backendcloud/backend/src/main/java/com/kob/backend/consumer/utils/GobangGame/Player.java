package com.kob.backend.consumer.utils.GobangGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId; // -1表示亲自出马，否则表示用AI打
    private String botCode;
    private List<Integer> steps; // 每一步的位置

    public String getStepsString() {
        String res = "";
        Boolean flag = false;

        // 将棋子坐标转化为x,x,x....
        for(int d : steps) {
            if(!flag) {
                flag = true;
            } else {
                res += ",";
            }
            res += String.valueOf(d);
        }
        return res;
    }
}
