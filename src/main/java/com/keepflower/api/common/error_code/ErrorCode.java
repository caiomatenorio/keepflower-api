package com.keepflower.api.common.error_code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements CodeWithMessageKey {
    E000("error.unknown", 500),
    E001("error.unauthorized", 401),
    E002("error.validation", 400),
    E003("error.username-already-in-use", 409);

    private final String messageKey;
    private final int statusCode;

    @Nullable
    private Object[] messageArgs;

    public void setMessageArgs(Object... args) {
        this.messageArgs = args;
    }
}
