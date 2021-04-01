package main.controllers;

import main.api.response.AuthCaptchaResponse;
import main.api.response.LoginResponse;
import main.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(value = "/api/auth")
public class ApiAuthController {
    private final AuthService authService;

    @Autowired
    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("")
//    ResponseEntity<String> getAuth() {
//        return new ResponseEntity<>("/api/auth/", HttpStatus.OK);
//    }
    ResponseEntity<String> getAuth() {
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body("Custom header set");
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

}
