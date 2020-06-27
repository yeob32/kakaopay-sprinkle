package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.common.TokenUtil;
import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.dao.SprinkleRepository;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class SprinkleApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SprinkleRepository sprinkleRepository;
    @Autowired
    ChatRepository chatRepository;

    private final String token = TokenUtil.generateToken();

    User sprinkler;
    User receiver;
    Chat chat1;

    Sprinkle sprinkle;

    @BeforeEach
    public void setUp() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("sykim"));
        chat1 = chatRepository.save(new Chat());
        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat1)
                .amount(1000)
                .divideCount(3)
                .build();
        sprinkle.createSprinkle();
    }

    @Test
    @DisplayName("뿌리기 조회")
    public void mySprinkle() throws Exception {
        Sprinkle saveSprinkle = sprinkleRepository.save(sprinkle);
        mockMvc.perform(MockMvcRequestBuilders.get("/sprinkle")
                .header("X-USER-ID", sprinkler.getId())
                .header("X-ROOM-ID", chat1.getId())
                .param("token", saveSprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("뿌리기 등록")
    public void sprinkle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sprinkle")
                .header("X-USER-ID", "1")
                .header("X-ROOM-ID", "1")
                .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
