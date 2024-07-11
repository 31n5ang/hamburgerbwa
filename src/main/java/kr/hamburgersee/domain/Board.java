package kr.hamburgersee.domain;

import jakarta.persistence.*;
import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import kr.hamburgersee.dto.board.BoardDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @Embedded
    private Likable likable = new Likable(0, 0);

    @Embedded
    private At at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    // 생성 메소드
    public Board(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    // DTO 변환 메소드
    public static BoardDto getBoardDto(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                new Likable(
                        board.getLikable().getLikes(),
                        board.getLikable().getDislikes()
                ),
                new At(
                        board.getAt().getCreatedAt(),
                        board.getAt().getUpdatedAt()
                ),
                board.getStatus(),
                board.getMember().getId(),
                board.getMember().getNickname()
        );
    }

    public static Board getBoard(BoardDto boardDto, Member member) {
        return new Board(
                boardDto.getTitle(),
                boardDto.getContent(),
                member
        );
    }

    //
}
