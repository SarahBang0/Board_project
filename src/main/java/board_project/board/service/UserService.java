package board_project.board.service;

import board_project.board.domain.Role;
import board_project.board.domain.User;
import board_project.board.dto.UserJoinRequestDto;
import board_project.board.dto.UserResponseDto;
import board_project.board.exception.DuplicateResourceException;
import board_project.board.exception.ErrorCode;
import board_project.board.exception.ResourceNotFoundException;
import board_project.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public Long join(UserJoinRequestDto dto) {
        //중복 이메일 검증
        validateDuplicateUser(dto.getEmail());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.createUser(dto.getName(), dto.getEmail(), encodedPassword, LocalDateTime.now(), Role.USER);
        userRepository.save(user);
        return user.getId();
    }

    // 중복 이메일 검증 로직
    private void validateDuplicateUser(String email) {
        if(!userRepository.findByEmail(email).isEmpty()) {
            log.warn("[중복 이메일] - 이메일 : {}", email);
            throw new DuplicateResourceException(ErrorCode.DUPLICATE_EMAIL,
                    "이미 존재하는 이메일입니다.");
        }
    }

    // 회원 조회
    public UserResponseDto findUser(Long userId) {
        User user = userRepository.findOne(userId).orElseThrow(
                ()-> {
                    log.warn("[회원 조회 실패] 존재하지 않는 회원 - 회원 ID : {}", userId);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,
                            "해당 회원이 존재하지 않습니다. 회원 ID: " + userId);
                });
        return new UserResponseDto(user);
    }

    // 전체 회원 조회
    public List<UserResponseDto> findUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::new)
                .toList();
    }

    // 이메일로 회원 조회
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> {
                    log.warn("[회원 조회 실패] 존재하지 않는 회원 - 회원 Email : {}", email);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND,
                            "해당 회원이 존재하지 않습니다. 회원 Email: " + email);
                });
        return new UserResponseDto(user);
    }
}
