package com.lifeAI.LifeAI.services;


import com.lifeAI.LifeAI.exceptions.common.ApiException;

public interface ExceptionService {

    void log(ApiException runtimeException);

    void log(RuntimeException runtimeException, int statusCode);
}
