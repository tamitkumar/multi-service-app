package com.user.module.exception;

import com.user.module.utils.UserConstant;
import lombok.Getter;

import java.io.Serial;

import static com.user.module.utils.UserConstant.HYPHEN;

public class UserSystemException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4711404730678356597L;

    @Getter
    private final String code;
    private final String message;

    public UserSystemException(Exception exception) {
        super(exception);
        String builder = UserConstant.HTTP_CODE_500 + HYPHEN +
                UserConstant.HTTP_MSG_500;
        this.code = UserConstant.HTTP_CODE_500;
        this.message = builder;
    }

    public UserSystemException(String code, String message) {
        super(message);
        String builder = code + HYPHEN + message;
        this.code = code;
        this.message = builder;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
