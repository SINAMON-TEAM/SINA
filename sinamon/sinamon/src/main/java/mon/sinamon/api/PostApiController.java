package mon.sinamon.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.repository.MemberRepository;
import mon.sinamon.repository.PostRepository;
import mon.sinamon.service.PostService;
import org.springframework.web.bind.annotation.*;

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
    private final MemberRepository memberRepository;


    // 게시글 작성
    @PostMapping("/api/posts/create")
    public CreatePostResponse createPost(@RequestBody @Valid CreatePostRequest request) {

        // request에서 받은 회원정보를 member 객체로 생성
        Post post = new Post();

        post.setMember(memberRepository.findOne(request.getMember_id()));
        post.setType(request.getType());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setPost_date(LocalDateTime.now());
        post.setView(request.getView());
        post.setLike_count(request.getLike_count());


        Long id = postService.join(post);
        return new CreatePostResponse(id);
    }


    // 전체 게시글 조회
    @GetMapping("/api/posts")
    public List<PostDto> getAllPost() {
        List<Post> posts = postRepository.findAllWithMember();
        List<PostDto> result = posts.stream()
                .map(p -> new PostDto(p))
                .collect(Collectors.toList());
        return result;
    }


    // 회원 id로 게시글 조회
    @GetMapping("/api/posts/{id}") // url에서 id값을 받아 인자로 활용
    public List<PostDto> getPostByMemberId(@PathVariable Long id) {

        List<Post> posts = postRepository.findByMemberId(id);
        List<PostDto> result = posts.stream()
                .map(p -> new PostDto(p))
                .collect(Collectors.toList());
        return result;
    }





    /*******************************여기까지 api 함수 이 아래는 api 함수들이 쓰는 함수들*********************************/







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

            private Long member_id;

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
