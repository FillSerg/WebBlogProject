package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import main.models.Post;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {
    private Long count;
    private List<PostForResponse> posts;

    public PostResponse() {
        count = 0L;
        posts = new ArrayList<>();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<PostForResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostForResponse> posts) {
        this.posts = posts;
    }
}
