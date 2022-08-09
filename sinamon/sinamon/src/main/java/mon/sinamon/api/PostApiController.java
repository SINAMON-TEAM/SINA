package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.repository.PostRepository;
import mon.sinamon.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;
    private final PostRepository postRepository;


    // 게시글 작성
    @PostMapping("/api/posts/create")
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest request) {

        // request에서 받은 회원정보를 member 객체로 생성
        Post post = new Post();
        post.setMember(request.getMember());
        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setPost_date(LocalDateTime.now());
        post.setView(request.getView());
        post.setLike_count(request.getLike_count());


        Long id = postService.join(post);
        return new CreatePostResponse(id);
    }


    @GetMapping("/api/v3/simple-orders")
    public List<PostDto> ordersV3() {
        List<Post> posts = postRepository.findAllWithMember();
        List<PostDto> result = posts.stream()
                .map(p -> new PostDto(p))
                .collect(Collectors.toList());
        return result;
    }


//    // 전체 회원 조회
//    @GetMapping("/api/members")
//    public Result lookupMember() {
//        List<Member> findMembers = memberService.findMembers(); // 회원 목록을 List로 받아옴
//        List<MemberDto> collect = findMembers.stream()
//                .map(m -> new MemberDto(m.getName(),m.getPhone(),m.getNickname(),m.getAddress().getAddress(),m.getAddress().getZipcode()))
//                .collect(Collectors.toList()); //Member -> DTO 변환
//        return new Result(collect.size(), collect);
//    }

    // Json 형식으로 반환을 할 때 Json Array를 그대로 반환하기보다는 이렇게 Result라는 틀로 한번 감싸서 반환하는 것이 유연성에 도움이 된다고 함
    // 예시를 위해 Result 클래스의 멤버로 회원 수를 나타내는 변수 count를 만들었음 이런식으로 배열 외에 다른 필요한 정보 함께 반환 가능
//    @Data
//    @AllArgsConstructor
//    static class Result<T> {
//        private int count;
//        private T data;
//    }

    // 게시글 조회용 DTO, 나중에 수정하거나 여러개 만들어서 사용할 것
    @Data
    @AllArgsConstructor
    static class PostDto {

        private Long id;

        private Long member_id;

        private String type;
        private String title;
        private String text;
        private LocalDateTime post_date;
        private int view;
        private int like_count;

        public PostDto(Post post) {
            id = post.getId();
            member_id = post.getMember().getMember_id();
            type = post.getType();
            title = post.getTitle();
            text = post.getText();
            post_date = post.getPost_date();
            view = post.getView();
            like_count = post.getLike_count();
        }
    }
//
//

        // 게시글 작성 api 함수가 인자로 받을 클래스
        @Data
        static class CreatePostRequest {
            //private Long id;

            private Member member;

            private String type;
            private String title;
            private String text;
            private int view = 0;
            private int like_count = 0;
        }

        // 게시글 작성 api가 반환하는 클래스, 지금은 id만 반환하도록 하고 있으나 이후에 수정 가능
        @Data
        static class CreatePostResponse {
            private Long id;

            public CreatePostResponse(Long id) {
                this.id = id;
            }
        }


}
