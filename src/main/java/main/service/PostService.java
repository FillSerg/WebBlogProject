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
        switch (mode) {
            case "early":
//                sortedByMode = PageRequest.of(page, limit, Sort.by("time").ascending());
                sortedByMode = PageRequest.of(page, limit);
                posts = postRepository.sortAllWithTime(sortedByMode);
                break;
            case "popular":
                sortedByMode = PageRequest.of(page, limit);
                posts = postRepository.sortAllWithPostCommentList(sortedByMode);
                break;
            case "best":
                sortedByMode = PageRequest.of(page, limit);
                posts = postRepository.sortAllWithPostLike(sortedByMode);
                break;
            default:
                sortedByMode = PageRequest.of(page, limit);
                posts = postRepository.reversedAllWithTime(sortedByMode);
                break;
        }
//        postResponse.setPosts(postRepository.findAll());
//        postResponse.setCount(postResponse.getPosts().size());
        postResponse = convertToPostResponse(posts);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * конвертируем List<Post> postList -> postForResponse
     */
    private PostResponse convertToPostResponse(Page<Post> postList) {
        List<PostForResponse> listPosts = new ArrayList<>();
        postList.get().forEach(post -> {
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
        postResponse.setCount(postList.getTotalElements());
        postResponse.setPosts(listPosts);
        return postResponse;
    }

    /**
     * Расчёт страницы.
     */
    private int getPageByOffsetAndLimit(int limit, int offset) {
        return offset / limit;
    }

    public ResponseEntity<PostResponse> searchPosts(int limit, int offset, String query) {
        Pageable sortedByMode;
        int page = getPageByOffsetAndLimit(limit, offset);
        PostResponse postResponse = new PostResponse();
        sortedByMode = PageRequest.of(page, limit, Sort.by("time").ascending());
        Page<Post> postPage;
        if (query == null || query.isEmpty()) {
            return posts(0,10,"recent");
//            postPage = postRepository.findAll(sortedByMode);
        } else {
            postPage = postRepository.findByTextAndTitlePost(sortedByMode, query);
        }
        postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }

    /*public ResponseEntity<PostResponse> posts(int limit, int offset, String mode) {
        Page<Post> postPage = getPostFromDb(postRepository, limit, offset, mode);
        PostResponse postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }*/
}
