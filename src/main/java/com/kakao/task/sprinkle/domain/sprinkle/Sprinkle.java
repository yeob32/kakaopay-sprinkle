package com.kakao.task.sprinkle.domain.sprinkle;

import com.kakao.task.sprinkle.domain.sprinkle.exception.DuplicateReceiveException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.ExpiredMySprinkleException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.ExpiredSprinkleException;
import com.kakao.task.sprinkle.domain.sprinkle.exception.VerifyReceiver;
import com.kakao.task.sprinkle.global.common.TokenUtil;
import com.kakao.task.sprinkle.domain.dividend.Dividend;
import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sprinkle")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Sprinkle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sprinkle_id")
    private Long id;

    @Column(name = "token", unique = true, nullable = false, length = 3)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Chat chat;

    @Column(name = "amount")
    private long amount;

    @Column(name = "divide_count")
    private int divideCount;

    @Column(name = "received_amount")
    private long receivedAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sprinkle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Dividend> dividends = new ArrayList<>();

    @Builder
    public Sprinkle(User user, Chat chat, long amount, int divideCount) {
        this.user = user;
        this.chat = chat;
        this.amount = amount;
        this.divideCount = divideCount;
    }

    public void validateExpired() {
        if (createdAt.isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new ExpiredSprinkleException(ErrorCode.RECEIVE_EXPIRED);
        }
    }

    public void validateReceive(User receiver) {
        validateQualified(receiver);
        receiveDuplication(receiver);
    }

    public void validateExpiredByRetreive() {
        if (createdAt.isBefore(LocalDateTime.now().minusDays(7))) {
            throw new ExpiredMySprinkleException(ErrorCode.RETREIVE_EXPIRED);
        }
    }

    private void validateQualified(User receiver) {
        if(user.equals(receiver)) {
            throw new VerifyReceiver(ErrorCode.VERIFY_RECEIVER);
        }
    }

    private void receiveDuplication(User receiver) {
        boolean duplication = this.dividends.stream()
                .filter(dividend -> !dividend.usable())
                .anyMatch(dividend -> dividend.getUser().equals(receiver));
        if(duplication) {
            throw new DuplicateReceiveException(ErrorCode.RECEIVE_DUPLICATION);
        }
    }

    public void createSprinkle() {
        this.divideAmount();
        this.token = TokenUtil.generateToken();
    }

    private void divideAmount() {
        this.dividends = this.devide().stream()
                .map(amount -> Dividend.builder()
                        .amount(amount)
                        .sprinkle(this)
                        .build())
                .collect(Collectors.toList());
    }

    private List<Long> devide() {
        List<Long> amounts = new ArrayList<>();
        for (int i = 0; i < divideCount; i++) {
            long rest = amount % divideCount;
            long devideAmount = amount / divideCount;
            if(rest > 0 && i == divideCount - 1) {
                devideAmount = devideAmount + rest;
            }

            amounts.add(devideAmount);
        }

        return amounts;
    }
}
