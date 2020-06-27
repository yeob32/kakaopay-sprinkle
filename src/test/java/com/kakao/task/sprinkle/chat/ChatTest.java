package com.kakao.task.sprinkle.chat;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ChatTest {

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;

    Chat chat = new Chat();

    @Test
    @DisplayName("채팅방 생성")
    public void addChatterRoom() {
        Chat savedChat = chatRepository.save(chat);
        Assertions.assertNotNull(savedChat);
    }

    @Test
    @DisplayName("채팅방 참여자 테스트")
    public void participation() {
        User user1 = userRepository.save(new User("1"));
        User user2 = userRepository.save(new User("2"));

        chat.addChatter(user1, user2);
        Chat savedChat = chatRepository.save(chat);

        Assertions.assertEquals(2, savedChat.getUsers().size());
        Assertions.assertEquals(2, userRepository.findAll().size());
    }



}
