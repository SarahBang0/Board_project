package board_project.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime joinedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    //==생성 메서드==//
    public static User createUser(String name, String email, String password, LocalDateTime joinedDate, Role role) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.joinedDate = joinedDate;
        user.role = role;
        return user;
    }

    //==변경 메서드==//
    public void changeProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
