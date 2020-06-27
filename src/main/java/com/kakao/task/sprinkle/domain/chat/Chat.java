package com.kakao.task.sprinkle.domain.chat;

import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Chat {

    @Id
    @GeneratedValue
    @Column(name = "chat_id")
    private UUID id;

    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public Chat(UUID id) {
        this.id = id;
    }

    public void addChatter(User... receviers) {
        users.addAll(Arrays.asList(receviers));
    }

    public void addChatter(List<User> receviers) {
        users.addAll(receviers);
    }

    public void checkContainsUser(final User receiver) {
        if(this.users.stream().noneMatch(user -> user == receiver)) {
            throw new InvalidChatterException(ErrorCode.INVALID_CHATTER);
        }
    }
}
