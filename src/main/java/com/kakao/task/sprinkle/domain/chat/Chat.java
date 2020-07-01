package com.kakao.task.sprinkle.domain.chat;

import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
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

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChatUser> chatUsers = new ArrayList<>();

    public static Chat createChat(List<ChatUser> chatUsers) {
        Chat chat = new Chat();
        chatUsers.forEach(chat::addChatUsers);
        return chat;
    }

    public void addChatUsers(ChatUser chatUser) {
        this.chatUsers.add(chatUser);
        chatUser.setChat(this);
    }

    public void checkContainsUser(final User receiver) {
        boolean exist = this.chatUsers.stream()
                .map(ChatUser::getUser)
                .anyMatch(user -> user.equals(receiver));
        if(!exist) {
            throw new InvalidChatterException(ErrorCode.INVALID_CHATTER);
        }
    }
}
