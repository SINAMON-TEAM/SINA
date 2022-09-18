package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post")
    private List<Chatroom> chatrooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PromiseStatus promise_status = PromiseStatus.READY; //[READY, COMP, CANCEL]

    private String type; // 배달음식인지 배송식품인지
    private String title; // 제목
    private String text; // 내용
    private LocalDateTime post_date = LocalDateTime.now(); // 작성 시간 //이걸 string으로 할지 이걸로 할지 고민좀 해봐야 함
    private String promise_date; // 약속 날짜
    private String promise_start_time; // 약속 시작 시간
    private String promise_end_time; // 약속 끝 시간
    private int max_people = 2;
    private int now_people = 1;

    private int view = 0;
    private int like_count = 0;
    private int price;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id2")
    private Member member2;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id3")
    private Member member3;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id4")
    private Member member4;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id5")
    private Member member5;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id6")
    private Member member6;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id7")
    private Member member7;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id8")
    private Member member8;
}
