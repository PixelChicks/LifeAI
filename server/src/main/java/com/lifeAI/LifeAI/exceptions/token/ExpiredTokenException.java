package com.lifeAI.LifeAI.exceptions.token;


import com.lifeAI.LifeAI.exceptions.common.UnauthorizedException;

public class ExpiredTokenException extends UnauthorizedException {
    public ExpiredTokenException() {
        super("Токенът е изтекъл!");
    }
}
