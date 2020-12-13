package com.iiq.rtbEngine.models;

import static com.iiq.rtbEngine.util.Common.*;

public enum UrlParam {
    ACTION_TYPE(ACTION_TYPE_VALUE),
    ATTRIBUTE_ID(ATTRIBUTE_ID_VALUE),
    PROFILE_ID(PROFILE_ID_VALUE),
    ;

    private final String value;

    private UrlParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}