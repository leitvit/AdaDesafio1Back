package com.ada.consumer.controller.record;

import java.util.Objects;

public record GenericErrorResponse(Integer httpStatus, String errorMsg) implements IErrorResponse {

    public GenericErrorResponse {
        Objects.requireNonNull(httpStatus);
        Objects.requireNonNull(errorMsg);
    }
}
