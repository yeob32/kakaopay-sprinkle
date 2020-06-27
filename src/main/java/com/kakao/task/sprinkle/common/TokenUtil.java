package com.kakao.task.sprinkle.common;

import java.util.UUID;

public class TokenUtil {

    public static String generateToken() {
        return UUID.randomUUID().toString().substring(0, 3);
    }
}
