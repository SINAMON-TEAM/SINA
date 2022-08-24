package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Chatroom {
    @Id @GeneratedValue
    @Column(name="chatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id1")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id2")
    private Member member2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @OneToMany(mappedBy = "chatroom")
    private List<Chatting> chattings = new ArrayList<>();


}
