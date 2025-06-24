package com.order.module.exception;

import com.order.module.utils.OrderConstant;
import lombok.Getter;

import java.io.Serial;

public class OrderSystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4711404730678356597L;

    @Getter
    private final String code;
    private final String message;

    public OrderSystemException(Exception exception) {
        super(exception);
        String builder = OrderConstant.HTTP_CODE_500 + OrderConstant.HYPHEN +
                OrderConstant.HTTP_MSG_500;
        this.code = OrderConstant.HTTP_CODE_500;
        this.message = builder;
    }

    public OrderSystemException(String code, String message) {
        super(message);
        String builder = code + OrderConstant.HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
