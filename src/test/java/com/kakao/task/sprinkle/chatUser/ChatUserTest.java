package com.kakao.task.sprinkle.chatUser;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@DataJpaTest
public class ChatUserTest {

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 등록 테스트")
    public void createUser() {
        User user1 = userRepository.save(new User("yeob32"));
        User user2 = userRepository.save(new User("yeob33"));

        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user1.getName(), "yeob32");

        Assertions.assertNotNull(user2);
        Assertions.assertEquals(user2.getName(), "yeob33");
    }

    @Test
    @DisplayName("채팅방 생성")
    public void addChatterRoom() {
        User user1 = userRepository.save(new User("yeob32"));
        User user2 = userRepository.save(new User("yeob33"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(Arrays.asList(user1, user2));

        Chat chat = Chat.createChat(chatUsers);
        Chat savedChat = chatRepository.save(chat);

        Assertions.assertNotNull(savedChat);
        Assertions.assertEquals(2, savedChat.getChatUsers().size());
    }

    @Test
    @DisplayName("채팅방 참여자 테스트")
    public void participation() {
        User user1 = userRepository.save(new User("yeob32"));
        User user2 = userRepository.save(new User("yeob33"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(Arrays.asList(user1, user2));

        Chat chat = Chat.createChat(chatUsers);
        Chat savedChat = chatRepository.save(chat);

        List<User> users = userRepository.findAll();
        boolean allmatch = users.stream()
                .allMatch(user ->
                        savedChat.getChatUsers().stream()
                                .anyMatch(chatUser -> user.equals(chatUser.getUser()))
                );
        Assertions.assertTrue(allmatch);
    }
}
