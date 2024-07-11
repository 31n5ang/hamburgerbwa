package kr.hamburgersee.domain;

import jakarta.persistence.*;
import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import kr.hamburgersee.dto.comment.CommentDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Lob
    private String content;

    @Embedded
    private Likable likable;

    @Embedded
    private At at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성 메소드
    public Comment(String content, Likable likable, Board board, Member member) {
        this.content = content;
        this.likable = likable;
        this.board = board;
        this.member = member;
    }

    // DTO 변환 메소드
    public static CommentDto getCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getBoard().getId(),
                comment.getContent(),
                comment.getMember().getNickname(),
                comment.getLikable(),
                comment.getAt()
        );
    }
}
