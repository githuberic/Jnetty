package com.lgq.jnetty.protocol;

import com.lgq.nio.common.util.JsonUtil;
import lombok.Data;

/**
 * @author lgq
 */
@Data
public class JsonMsg {
    private int id;
    private String content;

    /**
     * 在通用方法中，使用阿里FastJson转成Java对象
     *
     * @param json
     * @return
     */
    public static JsonMsg parseFromJson(String json) {
        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }

    /**
     * 在通用方法中，使用谷歌Gson转成字符串
     *
     * @return
     */
    public String convertToJson() {
        return JsonUtil.pojoToJson(this);
    }
}
