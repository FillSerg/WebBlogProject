package main.controllers;

import main.api.response.PostResponse;
import main.models.Post;
import main.repository.PostRepository;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {
    private final PostService postService;
    /*private final PostRepository postRepository;*/

    @Autowired
    public ApiPostController(PostService postService/*, PostRepository postRepository*/) {
        this.postService = postService;
        /*this.postRepository = postRepository;*/
    }

    @GetMapping("")
    public ResponseEntity<PostResponse> getPosts(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode) {

        return postService.posts(limit, offset, mode);
    }

    @GetMapping("/search")
    public ResponseEntity<PostResponse> searchPosts(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "query", required = false, defaultValue = "") String query) {
        return postService.searchPosts(limit, offset, query);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostResponse> getPostsByDate(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "date" , required = false) String date) {
        return postService.postsByDate(limit, offset, date);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostResponse> getPostsByTag(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "tag", defaultValue = "") String tag) {
        return postService.postsByTag(limit, offset, tag);
    }


}
