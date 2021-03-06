package com.kakao.task.sprinkle.domain.sprinkle.dto;

import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SprinkleDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Req {
        private long userId;
        private UUID roomId;
        private long amount;
        private int divideCount;

        @Builder
        public Req(long userId, UUID roomId, long amount, int divideCount) {
            this.userId = userId;
            this.roomId = roomId;
            this.amount = amount;
            this.divideCount = divideCount;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Res {
        private String token;

        public Res(Sprinkle sprinkle) {
            this.token = sprinkle.getToken();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyRes {
        private String token;
        private LocalDateTime createdAt;
        private long amount;
        private int divideCount;
        private long receivedAmount;
        private List<MyDividend> dividends;

        @Builder
        public MyRes(Sprinkle sprinkle, List<Dividend> dividends) {
            this.token = sprinkle.getToken();
            this.createdAt = sprinkle.getCreatedAt();
            this.amount = sprinkle.getAmount();
            this.divideCount = sprinkle.getDivideCount();
            this.receivedAmount = sprinkle.getReceivedAmount();
            this.dividends = dividends.stream()
                    .filter(dividend -> !dividend.usable())
                    .map(MyDividend::new)
                    .collect(Collectors.toList());
        }

        @Getter
        public static class MyDividend {
            private final long id;
            private final long amount;

            public MyDividend(Dividend dividend) {
                this.id = dividend.getUser().getId();
                this.amount = dividend.getAmount();
            }
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MyReq {
        private long userId;
        private UUID roomId;
        private String token;

        @Builder
        public MyReq(long userId, UUID roomId, String token) {
            this.userId = userId;
            this.roomId = roomId;
            this.token = token;
        }
    }
}
