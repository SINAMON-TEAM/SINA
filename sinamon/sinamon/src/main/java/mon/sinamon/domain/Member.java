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
    private String email;
    //private String address;

    @Embedded
    private Address address = new Address("default","default","default","default");
   // private Address address;


    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();


    /*public Member(String username, String password) {
        this.password = password;
        this.username = username;
    }*/
}
