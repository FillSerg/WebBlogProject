package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUserResponse {
    private int id;
    private String name;
    private String photo;
}
