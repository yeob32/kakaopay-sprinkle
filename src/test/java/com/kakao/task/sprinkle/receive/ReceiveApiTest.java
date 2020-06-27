package com.kakao.task.sprinkle.receive;

import com.kakao.task.sprinkle.domain.chat.Chat;
import com.kakao.task.sprinkle.domain.chat.ChatRepository;
import com.kakao.task.sprinkle.domain.sprinkle.Sprinkle;
import com.kakao.task.sprinkle.domain.sprinkle.application.SprinkleService;
import com.kakao.task.sprinkle.domain.sprinkle.dto.SprinkleDto;
import com.kakao.task.sprinkle.domain.user.User;
import com.kakao.task.sprinkle.domain.user.UserRepository;
import com.kakao.task.sprinkle.global.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiveApiTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    SprinkleService sprinkleService;

    User sprinkler1;
    User receiver;
    Chat chat;
    Sprinkle saveSprinkle1;

    @BeforeEach
    public void setUp() {
        sprinkler1 = userRepository.save(new User("yeob32"));
        receiver = userRepository.save(new User("sykim"));

        chat = new Chat();
        chat.addChatter(receiver, sprinkler1);
        chat = chatRepository.save(chat);

        SprinkleDto.Req req = SprinkleDto.Req.builder().roomId(chat.getId()).userId(sprinkler1.getId()).amount(1000).divideCount(3).build();
        saveSprinkle1 = sprinkleService.createSprinkle(req);
    }

    @Test
    @DisplayName("받기")
    public void receive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("받은 거 또 받기")
    public void receiveWithDuplicationException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.RECEIVE_DUPLICATION.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("내가 뿌린거 내가 받기")
    public void receiveWithVerifyReceiverException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler1.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.VERIFY_RECEIVER.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("받고 나서 해당 뿌리기 조회")
    public void receiveAndMySprinkle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/receive")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", receiver.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/sprinkle")
                .header("X-ROOM-ID", chat.getId())
                .header("X-USER-ID", sprinkler1.getId())
                .param("token", saveSprinkle1.getToken()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.receivedAmount").isNumber())
                .andDo(MockMvcResultHandlers.print());
    }
}
