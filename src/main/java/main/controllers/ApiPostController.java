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
    /*@ResponseBody*/
    public /*List<Post>*/ ResponseEntity<PostResponse> getPosts(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode) {

       /* PostResponse postResponse = new PostResponse();
        postResponse.setPosts(postRepository.findAll());
        System.out.println(postResponse.getPosts());
        postResponse.setCount(postResponse.getPosts().size());
        return ResponseEntity.ok(postResponse);*/
//        return postRepository.findAll();
        return postService.posts(limit, offset, mode);
    }

    /*@GetMapping("")
    ResponseEntity<PostResponse> getPost() {
        return ResponseEntity.ok(new PostResponse());
    }*/

}
