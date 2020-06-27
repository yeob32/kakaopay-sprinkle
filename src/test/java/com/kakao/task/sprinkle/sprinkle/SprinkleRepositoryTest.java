package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SprinkleRepositoryTest {

    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("뿌리기 등록")
    public void createDonate() {
        User user = new User("yeob32");
        User savedUser = userRepository.save(user);
        Chat chat = new Chat();
        chat.addChatter(user);
        Chat savedChat = chatRepository.save(chat);

        Sprinkle sprinkle = Sprinkle.builder()
                .chat(savedChat)
                .user(savedUser)
                .amount(1000)
                .divideCount(3)
                .build();
        sprinkle.createSprinkle();

        Sprinkle savedSprinkle = sprinkleRepository.save(sprinkle);
        Assertions.assertNotNull(savedSprinkle);
        Assertions.assertEquals(savedSprinkle.getAmount(), 1000);
        Assertions.assertEquals(savedSprinkle.getDivideCount(), 3);

        User sprinkleUser = savedSprinkle.getUser();
        Assertions.assertEquals(sprinkleUser, savedUser);
        Assertions.assertEquals(sprinkleUser.getName(), savedUser.getName());

        Chat sprinkleChat = savedSprinkle.getChat();
        Assertions.assertEquals(sprinkleChat, savedChat);
        Assertions.assertEquals(sprinkleChat.getId(), savedChat.getId());
    }
}
