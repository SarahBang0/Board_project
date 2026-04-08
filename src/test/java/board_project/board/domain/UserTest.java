package board_project.board.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void 회원_생성_테스트() {
        //given
        User user = User.createUser("회원A", "spring@gmail.com", "1234", LocalDateTime.now(), Role.USER);

        //when & then
        assertThat(user.getName()).isEqualTo("회원A");
        assertThat(user.getEmail()).isEqualTo("spring@gmail.com");
    }

    @Test
    void 회원_수정_테스트() {
        //given
        User user = User.createUser("회원A", "spring@gmail.com", "1234", LocalDateTime.now(), Role.USER);

        //when
        user.changeProfile("회원B", "change@gmail.com");

        //then
        assertThat(user.getName()).isEqualTo("회원B");
        assertThat(user.getEmail()).isEqualTo("change@gmail.com");

    }

}