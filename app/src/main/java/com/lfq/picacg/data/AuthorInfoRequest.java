package com.lfq.picacg.data;

import lombok.Data;

@Data
public class AuthorInfoRequest {

    /**
     * number : 0
     * code : 0
     * sex : 男
     * nickname : 三号测试员
     * img_source : http://192.168.0.104:8080/image/source/head/8
     * img_small : http://192.168.0.104:8080/image/small/head/8
     * word : 我是三号测试员
     */

    public int number;
    public int code;
    public String sex;
    public String nickname;
    public String img_source;
    public String img_small;
    public String word;
}
