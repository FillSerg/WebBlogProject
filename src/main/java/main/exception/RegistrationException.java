package main.exception;

import lombok.Data;
import main.api.response.RegisterResponse;

@Data
public class RegistrationException extends RuntimeException {
    private RegisterResponse registerErrorResponse;

    public RegistrationException(RegisterResponse registerErrorResponse) {
        this.registerErrorResponse = registerErrorResponse;
    }
}
