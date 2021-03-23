package main.api.response;

import lombok.Data;

@Data
public class PostForResponse {
    private int id;
    private Long timestamp;
    private UserResponse user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public PostForResponse(int id, Long timestamp, UserResponse user, String title, String announce, int likeCount, int dislikeCount, int commentCount, int viewCount) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
    }
}


