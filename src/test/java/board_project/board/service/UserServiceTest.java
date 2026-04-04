package board_project.board.service;

import board_project.board.domain.User;
import board_project.board.dto.UserJoinRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;


    @Test
    void 회원가입_조회_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com");

        //when
        User findUser = userRepository.findOne(userId);

        //then
        assertThat(findUser.getId()).isEqualTo(userId);
        assertThat(findUser.getName()).isEqualTo("회원A");
    }


    @Test
    void 중복_회원_예외_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com");

        //when & then
        UserJoinRequestDto userDto2 = new UserJoinRequestDto("회원B", "spring@gmail.com");

        IllegalStateException e = assertThrows(IllegalStateException.class,
                ()->userService.join(userDto2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }

    @Test
    void 회원_전체_조회_테스트() {
        //given
        Long userId1 = getUserId("회원A", "spring@gmail.com");
        Long userId2 = getUserId("회원B", "jpa@gmail.com");

        //when
        List<UserResponseDto> users = userService.findUsers();

        //then
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("name").containsExactlyInAnyOrder("회원A", "회원B");
    }

    private Long getUserId(String name, String email) {
        UserJoinRequestDto userDto1 = new UserJoinRequestDto(name, email);
        Long userId = userService.join(userDto1);
        return userId;
    }
}