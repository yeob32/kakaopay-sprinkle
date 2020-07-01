package com.kakao.task.sprinkle.domain.dividend;

import com.kakao.task.sprinkle.domain.sprinkle.exception.DuplicateReceiveException;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "dividend")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dividend_id")
    private Long id;

    @Column(name = "amount")
    private long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id")
    private Sprinkle sprinkle;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Dividend(long amount, Sprinkle sprinkle, User user) {
        this.amount = amount;
        this.sprinkle = sprinkle;
        this.user = user;
    }

    public void allotMoney(User receiver) {
        if(!usable()) {
            throw new DuplicateReceiveException(ErrorCode.RECEIVE_DUPLICATION);
        }

        this.user = receiver;
    }

    public boolean usable() {
        return this.user == null;
    }
}
