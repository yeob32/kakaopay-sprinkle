package com.kakao.task.sprinkle.domain.sprinkle.dto;

import com.kakao.task.sprinkle.domain.dividend.Dividend;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiveDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        private String token;
        private long userId;
        private UUID roomId;

        @Builder
        public Req(String token, long userId, UUID roomId) {
            this.token = token;
            this.userId = userId;
            this.roomId = roomId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private long receivedAmount;

        public Res(Dividend dividend) {
            this.receivedAmount = dividend.getAmount();
        }
    }
}
