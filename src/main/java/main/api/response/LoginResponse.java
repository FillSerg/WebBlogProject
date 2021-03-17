package main.api.response;

import org.springframework.stereotype.Component;

@Component
public class LoginResponse {
    private boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}

