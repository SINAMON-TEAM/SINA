package mon.sinamon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int esg_level=1;  //esg 레벨
    private int esg_point=0;  //esg 포인트 (일단 100까지로하자)
    //private String address;
   // private String logitude;    //경도(x축)
    //private String latitude;    //위도(y축)
  //  private String zipcode; //우편번호
   // private String more_address; //추가 주소
   // private String last_address; //참고사항



    @Embedded
    private Address address = new Address("default","default","default","default","default","default");
   // private Address address;


    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();


    /*public Member(String username, String password) {
        this.password = password;
        this.username = username;
    }*/
}
