package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Likes;
import mon.sinamon.domain.Member;
import mon.sinamon.domain.Post;
import mon.sinamon.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /*
     * 게시물 작성
     * */
    @Transactional
    public Long join(Post post){
        postRepository.save(post);
        return post.getId();
    }

    //게시글 전체 조회
    public List<Post> findAllPosts() {return postRepository.findAll();}

    //게시글 id로 조회
    public Post findPostById(Long id) {return postRepository.findOne(id);}

    //회원이 만든 id로 게시글 조회
    public List<Post> findPostByMemberMakingId(String id) {return postRepository.findByMemberMakingId(id);}

    //회원 id로 게시글 조회
    public List<Post> findPostByMemberId(Long id) {return postRepository.findByMemberId(id);}

    //게시글 조회수 증가시키기
    @Transactional
    public void updateViewCount(Post post, int view_count){
        post.setView(view_count);
    }

    //좋아요 목록 삭제
    @Transactional
    public void removePost(Post post){
        postRepository.remove(post);
    }

    //게시글 수정
    @Transactional
    public void updatePost(Post post,String promise_start_time, String promise_end_time, String title, String text, String type){
        post.setPromise_start_time(promise_start_time);
        post.setPromise_end_time(promise_end_time);
        post.setTitle(title);
        post.setText(text);
        post.setType(type);
    }



}
