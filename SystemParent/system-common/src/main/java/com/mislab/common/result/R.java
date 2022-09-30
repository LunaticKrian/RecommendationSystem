package com.mislab.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    // 私有化构造器：
    private R() {
    }

    /**
     * @return r
     * @Func 返回成功
     */
    public static R SUCCESS() {
        R r = new R();
        r.setCode(ResponseEnum.success.getCode());
        r.setMessage(ResponseEnum.success.getMessage());
        return r;
    }

    /**
     * @return
     * @Func 返回失败
     */
    public static R ERROR() {
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    /**
     * @param responseEnum
     * @return
     * @Func 设置特的返回结果
     */
    public static R setResult(ResponseEnum responseEnum) {
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }

    /**
     * @param key
     * @param value
     * @return
     * @Func 向返回的R对象中设置数据
     */
    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    // 方法重载：
    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

    /**
     * @param message
     * @return
     * @Fun 向返回的R对象中设置返回消息
     */
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * @param code
     * @return
     * @Func 想返回的R对象中设置返回消息
     */
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }
}
