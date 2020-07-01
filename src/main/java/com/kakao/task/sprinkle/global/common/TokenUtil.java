package com.kakao.task.sprinkle.global.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenUtil {

    public static String generateToken() {
        return UUID.randomUUID().toString().substring(0, 3);
    }
}
