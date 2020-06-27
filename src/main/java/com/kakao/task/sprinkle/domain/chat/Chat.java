package com.kakao.task.sprinkle.domain.chat;

import com.kakao.task.sprinkle.domain.user.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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

    public void checkContainsUser(final User findUser) {
        if(this.users.stream().anyMatch(u -> u == findUser)) {
            throw new RuntimeException();
        }
    }
}
