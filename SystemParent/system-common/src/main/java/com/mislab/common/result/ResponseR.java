package com.mislab.common.result;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @Func 通用返回结果，服务端响应数据最终都会封装成为此对象
 */
@Data
public class ResponseR<T> {

    private Integer code;               // 编码
    private String msg;                 // 错误信息
    private T data;                     // 数据
    private Map map = new HashMap();    // 动态数据

    public static <T> ResponseR<T> success(T object) {
        ResponseR<T> r = new ResponseR<T>();
        r.data = object;
        r.code = HttpStatus.OK.value();  // 通过Spring提供的HttpStatus获取返回状态码
        return r;
    }

    public static <T> ResponseR<T> error(String msg) {
        ResponseR r = new ResponseR();
        r.msg = msg;
        r.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return r;
    }

    public ResponseR<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
