package mon.sinamon.service;

import lombok.RequiredArgsConstructor;
import mon.sinamon.domain.Chatroom;
import mon.sinamon.domain.Likes;
import mon.sinamon.domain.Post;
import mon.sinamon.repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;


    @Transactional
    public Long join(Likes likes){
        likeRepository.save(likes);
        return likes.getId();
    }

    public Likes findLikesByPostAndMember(Long post_id, Long member_id) {
       return likeRepository.findByPostAndMember(post_id, member_id);
    }

    //게시글 좋아요 수 1 증가
    @Transactional
    public void updateLikeCount(Post post, int like_count){
        post.setLike_count(like_count);
    }

    //좋아요 목록 삭제
    @Transactional
    public void removeLike(Likes likes){
        likeRepository.remove(likes);
    }
}
