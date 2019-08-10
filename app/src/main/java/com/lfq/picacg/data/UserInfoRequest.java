package com.lfq.picacg.data;

import lombok.Data;

import org.json.JSONException;
import org.json.JSONObject;

@Data
public class UserInfoRequest {

    /**
     * code : 0
     * sex : 1
     * id : 2
     * nickname : jysh
     * word : 厉害
     */

    public int code;
    public int sex;
    public int id;
    public String nickname;
    public String word;

    public UserInfoRequest(int sex, String nickname, String word) {
        this.sex = sex;
        this.nickname = nickname;
        this.word = word;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("nickname", nickname);
            obj.put("word", word);
            obj.put("sex", sex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
