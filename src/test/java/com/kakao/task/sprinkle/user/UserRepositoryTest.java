package com.kakao.task.sprinkle.user;

import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user1 = new User("yeob32");
    User user2 = new User("sykim");

    @Test
    @DisplayName("사용자 등록 테스트")
    public void createUser() {
        User savedUser1 = userRepository.save(user1);
        Assertions.assertNotNull(savedUser1);
        Assertions.assertEquals(savedUser1.getId(), 1);
        Assertions.assertEquals(savedUser1.getName(), "yeob32");

        User savedUser2 = userRepository.save(user2);
        Assertions.assertEquals(savedUser2.getId(), 2);
        Assertions.assertEquals(savedUser2.getName(), "sykim");
    }
}
