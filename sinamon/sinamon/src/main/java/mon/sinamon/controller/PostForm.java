package mon.sinamon.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class PostForm {

    @NotEmpty(message="제목은 필수 입니다")
    private String title;

    private String text;
    private Long post_id;



    private String type;
    private LocalDateTime post_date;
    private int view;
    private int like_count;
    private Long member_id;
}
