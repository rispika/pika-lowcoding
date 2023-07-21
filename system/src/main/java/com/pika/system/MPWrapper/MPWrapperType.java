package com.pika.system.MPWrapper;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum MPWrapperType {

    NONE,
    EQ,
    LIKE,
    LEFT_LIKE,
    RIGHT_LIKE;

    public static Set<MPWrapperType> keySet() {
        return Arrays.stream(values()).collect(Collectors.toSet());
    }

}
