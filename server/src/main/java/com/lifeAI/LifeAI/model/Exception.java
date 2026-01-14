package com.lifeAI.LifeAI.model;

import com.lifeAI.LifeAI.enums.ExceptionSeverity;
import com.lifeAI.LifeAI.exceptions.common.ApiException;
import com.lifeAI.LifeAI.model.baseEntity.BaseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exception extends BaseEntity {
    private Integer statusCode;
    private String exceptionType;
    private String exceptionMessage;
    private String stackTraceString;
    private String methodName;
    private String className;
    private Integer lineNumber;
    @Enumerated(EnumType.STRING)
    private ExceptionSeverity severity;

    public static Exception mapFromRuntimeException(RuntimeException runtimeException, int statusCode) {
        String exceptionType = runtimeException.getClass().getName();
        String exceptionMessage = runtimeException.getMessage();

        StackTraceElement[] stackTrace = runtimeException.getStackTrace();
        String stackTraceString = Arrays.toString(stackTrace);

        String methodName = stackTrace[0].getMethodName();
        String className = stackTrace[0].getClassName();
        int lineNumber = stackTrace[0].getLineNumber();

        return Exception.builder()
                .statusCode(statusCode)
                .exceptionType(exceptionType)
                .exceptionMessage(exceptionMessage)
                .stackTraceString(stackTraceString)
                .methodName(methodName)
                .className(className)
                .lineNumber(lineNumber)
                .severity(ExceptionSeverity.CRITICAL)
                .build();
    }

    public static Exception mapFromApiException(ApiException apiException) {
        Exception exception = mapFromRuntimeException(apiException, apiException.getStatusCode());
        exception.setSeverity(ExceptionSeverity.INFORMATIONAL);

        return exception;
    }
}
