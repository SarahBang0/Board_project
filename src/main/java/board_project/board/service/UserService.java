package board_project.board.service;

import board_project.board.domain.User;
import board_project.board.dto.UserJoinRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    @Transactional
    public Long join(UserJoinRequestDto userJoinRequestDto) {
        //중복 이메일 검증
        validateDuplicateUser(userJoinRequestDto.getEmail());
        User user = userJoinRequestDto.toEntity();
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
    public UserResponseDto findUser(Long userId) {
        User user = userRepository.findOne(userId);
        if(user == null) {
            throw new IllegalStateException("해당 회원이 존재하지 않습니다. 회원 Id: " + userId);
        }
        return new UserResponseDto(user);
    }

    // 전체 회원 조회
    public List<UserResponseDto> findUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }
}
