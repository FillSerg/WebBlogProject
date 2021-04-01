package main.service;

import main.api.response.PostForResponse;
import main.api.response.PostResponse;
import main.api.response.UserResponse;
import main.enums.ModerationStatus;
import main.models.Post;
import main.models.Tag;
import main.models.Tags2Posts;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.TagRepository;
import main.repository.Votes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostService(PostRepository postRepository, PostVoteRepository postVoteRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postVoteRepository = postVoteRepository;
        this.tagRepository = tagRepository;
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
        postResponse = convertToPostResponse(posts);
//        filter(posts);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * конвертируем List<Post> postList -> postForResponse +
     * фильтр (post.getIsActive() == 1 && post.getModerationStatus() ==
     */
    private PostResponse convertToPostResponse(Page<Post> postList) {
        List<PostForResponse> listPosts = new ArrayList<>();
        postList.get().filter(post -> (post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED))
                .forEach(post -> {
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

    // Спросить почему на фронт при отсутсвии query не выводится список постов
    public ResponseEntity<PostResponse> searchPosts(int limit, int offset, String query) {
        Pageable sortedByMode;
        int page = getPageByOffsetAndLimit(limit, offset);
        PostResponse postResponse = new PostResponse();
        sortedByMode = PageRequest.of(page, limit, Sort.by("time").ascending());
        Page<Post> postPage;
        if (query == null || query.isEmpty()) {
            return posts(0, 10, "recent");
//          postPage = postRepository.findAll(sortedByMode);
        } else {
            postPage = postRepository.findByTextAndTitlePost(sortedByMode, query);
        }
        postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }

    public ResponseEntity<PostResponse> postsByDate(int limit, int offset, String date) {
        Pageable sortedByMode = PageRequest.of(
                getPageByOffsetAndLimit(limit, offset),
                limit,
                Sort.by("time").descending());
//        2019-10-15
        Date beforeData = new Date();
        try {
            beforeData = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            //добавить лог/сообщение
            e.printStackTrace();
        }
        Date afterDate = new Date(beforeData.getTime() + 24 * 60 * 60 * 1000);
        Page<Post> postPage = postRepository.findAllByDate(sortedByMode, beforeData, afterDate);
        PostResponse postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }

    /*private Page<Post> filter(Page<Post> postPage){
        postPage.stream()*//*.filter(Objects::nonNull)*//*
                .filter(post -> (post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED)).
                        forEach(System.out::println);
                        *//*collect(Collectors.toList());*//*

        return postPage;
    }*/

    //Сортровка по time не работает
    public ResponseEntity<PostResponse> postsByTag(int limit, int offset, String tag) {
        Pageable sortedByMode = PageRequest.of(
                getPageByOffsetAndLimit(limit, offset),
                limit,
                Sort.by("time").ascending());
        Tag findTag = tagRepository.findTagByName(tag);
        if (findTag == null) {
            return ResponseEntity.ok(new PostResponse());
        }
        List<Post> postList = findTag.getTags2PostsList()
                .stream()
                .map(Tags2Posts::getPostId)
                .collect(Collectors.toList());
        if (postList.isEmpty()) {
            return ResponseEntity.ok(new PostResponse());
        }
        Page<Post> postPage = new PageImpl<>(postList, sortedByMode, postList.size());
        PostResponse postResponse = convertToPostResponse(postPage);
        return ResponseEntity.ok(postResponse);
    }

}
