package main.controllers;

import main.api.response.PostResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(value = "/api/post")
public class ApiPostController {

    @GetMapping("")
    ResponseEntity<PostResponse> getPost() {
        return ResponseEntity.ok(new PostResponse());
    }

}
