package com.kakao.task.sprinkle.chat;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    ChatRepository chatRepository;

    Chat chat = new Chat();

    @Test
    public void createChatRoom() {
        Chat savedChat = chatRepository.save(chat);
        Assertions.assertNotNull(savedChat);
    }
}
