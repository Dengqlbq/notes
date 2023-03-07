package com.deng.contentcenter.exception;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException() {
        super();
    }

    public TokenInvalidException(String s) {
        super(s);
    }
}
