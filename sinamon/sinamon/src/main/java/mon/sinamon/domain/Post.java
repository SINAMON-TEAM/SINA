package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="post")
@Getter @Setter
public class Post {
    @Id @GeneratedValue
    @Column(name="post_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")   //1대다 관계에서 연관관계의 주인은 다가 가진다
    @NonNull
    private Member member;

    private String type;
    private String title;
    private String text;
    private LocalDateTime post_date;
    private int view = 0;
    private int like_count = 0;
}
