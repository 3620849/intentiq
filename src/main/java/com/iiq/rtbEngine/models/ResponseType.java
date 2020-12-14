package com.iiq.rtbEngine.models;


import static com.iiq.rtbEngine.util.Common.TYPE_CAPPED;
import static com.iiq.rtbEngine.util.Common.TYPE_UNMATCHED;

public enum  ResponseType {
    UNMATCHED(TYPE_UNMATCHED),
    CAPPED(TYPE_CAPPED),
    ;

    private String value;

    ResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
