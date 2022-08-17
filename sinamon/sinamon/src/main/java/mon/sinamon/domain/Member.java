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
    private String name;
    private String nickname;
    private String major;
    private String password;
    private String phone;

    @Embedded
    private Address address;




}
