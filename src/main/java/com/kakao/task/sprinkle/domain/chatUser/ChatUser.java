package com.kakao.task.sprinkle.domain.chatUser;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "chat_user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser {

    @Id
    @GeneratedValue
    @Column(name = "chat_user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    public Chat chat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static List<ChatUser> createChatUsers(List<User> users) {
        return users.stream()
                .map(user -> {
                    ChatUser chatUser = new ChatUser();
                    chatUser.setUser(user);
                    return chatUser;
                })
                .collect(Collectors.toList());
    }

}
