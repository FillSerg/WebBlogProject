package main.service;

import main.api.response.PostForResponse;
import main.api.response.PostResponse;
import main.api.response.UserResponse;
import main.models.Post;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.Votes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostVoteRepository postVoteRepository) {
        this.postRepository = postRepository;
        this.postVoteRepository = postVoteRepository;
    }

    public ResponseEntity<PostResponse> posts(int limit, int offset, String mode) {
        Pageable sortedByMode;
        Page<Post> posts;
        int page = getPageByOffsetAndLimit(limit, offset);
        PostResponse postResponse = new PostResponse();
        List<Post>postList = new ArrayList<>();
        switch (mode) {
            case "early":
                sortedByMode = PageRequest.of(page, limit, Sort.by("time").ascending());
                postList = postRepository.sortAllWithTime();
                break;
            case "popular":
                sortedByMode = PageRequest.of(page, limit);
                postList = postRepository.sortAllWithPostCommentList();
                break;
            case "best":
                sortedByMode = PageRequest.of(page, limit);
                postList = postRepository.sortAllWithPostLike();
                break;
            //RECENT
            default:
                sortedByMode = PageRequest.of(page, limit, Sort.by("time").descending());
                postList = postRepository.reversedAllWithTime();
                break;
        }
//        postResponse.setPosts(postRepository.findAll());
//        postResponse.setCount(postResponse.getPosts().size());
//        System.out.println(postResponse.getPosts().size() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        postResponse = convertToPostResponse(postList);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * конвертируем List<Post> postList -> postResponse
     */
    private PostResponse convertToPostResponse(List<Post> postList) {
        List<PostForResponse> listPosts = new ArrayList<>();
        postList.forEach(post -> {
            Votes votes = postVoteRepository.getVotes(post.getId());
            UserResponse userResponse = new UserResponse(
                    post.getUser().getId(),
                    post.getUser().getName());
            listPosts.add(new PostForResponse(post.getId(),
                    post.getTime().getTime() / 1_000L,
                    userResponse,
                    post.getTitle(),
                    post.getText(),
                    votes.getLikes(),
                    votes.getDislikes(),
                    post.getPostCommentList().size(),
                    post.getViewCount())
            );
        });
        PostResponse postResponse = new PostResponse();
        postResponse.setCount(postList.size());
        postResponse.setPosts(listPosts);
        return postResponse;
    }

    /**
     * Вспомогательный метод расчёта страницы.
     */
    private int getPageByOffsetAndLimit(int limit,
                                        int offset) {
        return offset / limit;
    }

    /*public ResponseEntity<PostResponse> posts(int limit, int offset, String mode) {
        Page<Post> postPage = getPostFromDb(postRepository, limit, offset, mode);
        PostResponse postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }*/
}
