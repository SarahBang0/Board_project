package board_project.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "BOARDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    @Column(name = "board_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();


    //== 변경 메서드==//
    private void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void remove() {
        if(this.user!=null) {
            this.user.getBoards().remove(this);
        }
    }

    //==생성 메서드==//
    public static Board createBoard(String title, String content, User user) {
        Board board = new Board();
        board.title = title;
        board.content = content;
        board.setUser(user);
        return board;
    }



}
