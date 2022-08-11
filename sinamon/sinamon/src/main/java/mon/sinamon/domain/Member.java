package mon.sinamon.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long member_id;

    private String id;
    private String password;
    private String name;
    private String phone;
    private String nickname;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();




}
