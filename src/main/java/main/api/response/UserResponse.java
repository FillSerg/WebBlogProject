package main.api.response;

import lombok.Data;

@Data
public class UserResponse {
    private int id;
    private String name;

    public UserResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
