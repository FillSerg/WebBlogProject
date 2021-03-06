package main.controllers;

import main.api.request.RegisterRequest;
import main.api.response.AuthCaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.RegisterResponse;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping(value = "/api/auth")
public class ApiAuthController<T> {
    private final AuthService authService;

    @Autowired
    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("")
    ResponseEntity<String> getAuth() {
        return new ResponseEntity<>("/api/auth/", HttpStatus.OK);
    }

    @GetMapping(value = "/check")
    public ResponseEntity<LoginResponse> check() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(false);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/captcha")
    public ResponseEntity<AuthCaptchaResponse> captcha() {
        return authService.captcha();
    }

    @PostMapping("/register")
    public ResponseEntity<? extends RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return (ResponseEntity<? extends RegisterResponse>) authService.register(registerRequest);
    }


}
