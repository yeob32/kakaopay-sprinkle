package com.kakao.task.sprinkle.sprinkle;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.chatUser.ChatUser;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class SprinkleApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private SprinkleRepository sprinkleRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        sprinkleRepository.deleteAll();

        setUpData();
    }

    User sprinkler;
    User receiver;
    Chat chat;
    Sprinkle sprinkle;

    @Test
    @DisplayName("뿌리기 조회")
    public void mySprinkle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sprinkle")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler.getId())
                .param("token", sprinkle.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("뿌리기 등록")
    public void sprinkle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sprinkle")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler.getId())
                .param("amount", "1000")
                .param("divideCount", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("@RequestParam Exception 테스트")
    public void sprinkleParameterException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sprinkle")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler.getId())
                .param("divideCount", "3"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required parameter"))
                .andDo(print());
    }

    private void setUpData() {
        sprinkler = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("yeob33"));
        List<ChatUser> chatUsers = ChatUser.createChatUsers(Arrays.asList(sprinkler, receiver));

        chat = Chat.createChat(chatUsers);
        chatRepository.save(chat);

        sprinkle = Sprinkle.builder()
                .user(sprinkler)
                .chat(chat)
                .amount(1000)
                .divideCount(3)
                .build();
        sprinkleRepository.save(sprinkle);
    }
}
