package board_project.board.service;

import board_project.board.domain.User;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    @Transactional
    public Long join(String name, String email) {
        //중복 이메일 검증
        validateDuplicateUser(email);
        User user = User.createUser(name, email);
        userRepository.save(user);
        return user.getId();
    }

    // 중복 이메일 검증 로직
    private void validateDuplicateUser(String email) {
        if(!userRepository.findByEmail(email).isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    // 회원 조회
    public User findUser(Long userId) {
        return userRepository.findOne(userId);
    }

    // 전체 회원 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
