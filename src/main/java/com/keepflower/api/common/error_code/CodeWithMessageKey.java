package com.keepflower.api.common.error_code;

import org.springframework.lang.Nullable;

public interface CodeWithMessageKey {
    String getMessageKey();

    @Nullable
    Object[] getMessageArgs();
}
