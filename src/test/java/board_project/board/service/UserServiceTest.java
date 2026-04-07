package board_project.board.service;

import board_project.board.domain.User;
import board_project.board.dto.UserJoinRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.exception.DuplicateResourceException;
import board_project.board.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;
    @Autowired BCryptPasswordEncoder passwordEncoder;


    @Test
    void 회원가입_조회_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com","1234");

        //when
        User findUser = userRepository.findOne(userId).orElseThrow();

        //then
        assertThat(findUser.getId()).isEqualTo(userId);
        assertThat(findUser.getName()).isEqualTo("회원A");
    }


    @Test
    void 중복_회원_예외_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com","1234");

        //when & then
        UserJoinRequestDto userDto2 = new UserJoinRequestDto("회원B", "spring@gmail.com", "1234");

        DuplicateResourceException e = assertThrows(DuplicateResourceException.class,
                ()->userService.join(userDto2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }


    @Test
    void 회원_전체_조회_테스트() {
        //given
        Long userId1 = getUserId("회원A", "spring@gmail.com","1234");
        Long userId2 = getUserId("회원B", "jpa@gmail.com","1234");

        //when
        List<UserResponseDto> users = userService.findUsers();

        //then
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).extracting("name").containsExactlyInAnyOrder("회원A", "회원B");
    }

    @Test
    void 비밀번호_암호화_테스트() {
        //given
        Long userId = getUserId("회원A", "spring@gmail.com", "1234");

        //when
        Optional<User> findUser = userRepository.findOne(userId);

        //then
        assertThat(findUser.get().getPassword()).isNotEqualTo("1234");
        assertThat(passwordEncoder.matches("1234", findUser.get().getPassword())).isTrue();
    }


    private Long getUserId(String name, String email, String password) {
        UserJoinRequestDto userDto1 = new UserJoinRequestDto(name, email,password);
        Long userId = userService.join(userDto1);
        return userId;
    }
}