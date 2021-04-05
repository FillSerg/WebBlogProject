package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterResponse {
    private boolean result;
    /*private String email;
    private String name;
    private String password;
    private String captcha;*/
//    private RegisterErrorResponse errors;

    public RegisterResponse(boolean result) {
        this.result = result;
    }

}
