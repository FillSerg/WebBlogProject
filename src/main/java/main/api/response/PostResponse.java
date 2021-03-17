package main.api.response;

import main.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostResponse {
    private Long count;
    private List<Post> posts;

    public PostResponse() {
        count = (long) 0;
        posts = new ArrayList<>();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
