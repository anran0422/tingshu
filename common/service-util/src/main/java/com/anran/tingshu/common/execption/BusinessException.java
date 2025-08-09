package com.anran.tingshu.common.execption;

import com.anran.tingshu.common.result.ResultCodeEnum;
import lombok.Getter;

/**
 * 自定义全局异常类
 *
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;


    /**
     * 赋值一个错误码，继承错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 接收错误枚举类型
     */
    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 接收枚举类型，并且自己赋值错误信息
     */
    public BusinessException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.code = resultCodeEnum.getCode();
    }
}
