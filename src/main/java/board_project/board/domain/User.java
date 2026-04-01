package board_project.board.domain;

import board_project.board.BoardApplication;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    //==생성 메서드==//
    public static User createUser(String name, String email) {
        User user = new User();
        user.name = name;
        user.email = email;
        return user;
    }

    //==변경 메서드==//
    public void changeProfile(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
