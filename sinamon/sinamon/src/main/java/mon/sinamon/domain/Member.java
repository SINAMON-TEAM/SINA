package mon.sinamon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@RequiredArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long member_id;

    private Long kakao_id;
    private String name;
    private String nickname="default";
    private String major="default";
    private String password="default";
    private String phone="default";
    private String email;
    //private String username;

    @Embedded
    private Address address = new Address("default","default");

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();


    /*public Member(String username, String password) {
        this.password = password;
        this.username = username;
    }*/
}
