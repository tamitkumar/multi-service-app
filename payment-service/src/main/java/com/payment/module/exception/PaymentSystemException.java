package com.payment.module.exception;

import com.payment.module.utils.PaymentConstant;
import lombok.Getter;

import java.io.Serial;

public class PaymentSystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4711404730678356597L;

    @Getter
    private final String code;
    private final String message;

    public PaymentSystemException(Exception exception) {
        super(exception);
        String builder = PaymentConstant.HTTP_CODE_500 + PaymentConstant.HYPHEN +
                PaymentConstant.HTTP_MSG_500;
        this.code = PaymentConstant.HTTP_CODE_500;
        this.message = builder;
    }

    public PaymentSystemException(String code, String message) {
        super(message);
        String builder = code + PaymentConstant.HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
