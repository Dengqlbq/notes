package com.deng.usercenter.exception;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException() {
        super();
    }

    public TokenInvalidException(String s) {
        super(s);
    }
}
