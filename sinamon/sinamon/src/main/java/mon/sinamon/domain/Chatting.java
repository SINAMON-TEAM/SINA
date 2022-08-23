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
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_writer_id")
    private Member post_writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="talker_id") //member_id2
    private Member talker;

    @Enumerated(EnumType.STRING)
    private MessageSender sender; //[READY, COMP, CANCEL]

    private String message;

    private LocalDateTime message_time = LocalDateTime.now();
}
