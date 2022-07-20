package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Promise {
    @Id @GeneratedValue
    @Column(name="promise_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToMany
    @JoinTable(name="promise_member",
    joinColumns= @JoinColumn(name="promise_id"),
    inverseJoinColumns = @JoinColumn(name="member_id"))
    private List<Member> members=new ArrayList<>();

    private int member_count;

    private LocalDateTime promise_time;

    @Enumerated(EnumType.STRING)
    private PromiseStatus status; //[READY, COMP, CANCEL]
}
