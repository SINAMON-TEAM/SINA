package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Chatting {
    @Id @GeneratedValue
    @Column(name="chatting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id1")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id2")
    private Member member2;

    private String message;

    private LocalDateTime message_time;
}
