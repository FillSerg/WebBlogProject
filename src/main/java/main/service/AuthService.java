package main.service;

import lombok.extern.slf4j.Slf4j;
import main.api.request.RegisterRequest;
import main.api.response.AuthCaptchaResponse;
import main.api.response.RegisterErrorResponse;
import main.api.response.RegisterResponse;
import main.repository.UserRepository;
import main.util.CaptchaUtil;
import main.models.CaptchaCode;
import main.repository.CaptchaCodesRepository;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    @Value("${captcha.delete.hours}")
    private int captchaDeleteHours;

    @Value("${password.min.length}")
    private int passwordMinLength;

    @Value("${password.restore.code.length}")
    private int passwordRestoreCodeLength;

    @Value("${captcha.length}")
    private int captchaLength;

    @Value("${captcha.width}")
    private int captchaWidth;

    @Value("${captcha.height}")
    private int captchaHeight;

    @Value("${captcha.secretCode.length}")
    private int captchaSecretCodeLength;

    private final PostRepository postRepository;
    private final CaptchaCodesRepository captchaCodesRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(PostRepository postRepository, CaptchaCodesRepository captchaCodesRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.captchaCodesRepository = captchaCodesRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<AuthCaptchaResponse> captcha() {
        CaptchaUtil captchaUtil = new CaptchaUtil(
                captchaLength,
                captchaWidth,
                captchaHeight,
                captchaSecretCodeLength);
        String code = captchaUtil.getCode();
        String secretCode = captchaUtil.getSecretCode();

        AuthCaptchaResponse authCaptchaResponse = new AuthCaptchaResponse();
        authCaptchaResponse.setSecret(secretCode);
        authCaptchaResponse.setImage(captchaUtil.getImageString());

        CaptchaCode captchaCodes = new CaptchaCode();
        captchaCodes.setCode(code);
        captchaCodes.setSecretCode(secretCode);
        captchaCodes.setTime(new Date());
        //???????????????? < 1 ??????
        deleteOldCaptcha();
        //???????????????????? ??????????
        captchaCodesRepository.save(captchaCodes);
        log.info("Captcha code: " + code + ". Secret code: " + secretCode);
        return ResponseEntity.ok(authCaptchaResponse);
    }

    private void deleteOldCaptcha() {
        Date date = new Date(new Date().getTime() - (long) captchaDeleteHours * 60 * 60 * 1000);
        List<CaptchaCode> captchaCodes = captchaCodesRepository.findAll();
        for (CaptchaCode captcha : captchaCodes
        ) {
            if (captcha.getTime().before(date)) {
                captchaCodesRepository.delete(captcha);
            }
        }
    }

    @Transactional
    public ResponseEntity<? extends RegisterResponse> register(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = validateRegisterRequest(registerRequest);

        if (registerResponse instanceof RegisterErrorResponse) {
            return ResponseEntity.ok(registerResponse);
        }
        log.info(registerRequest.getName() + " / " + registerRequest.getPassword() +
                " / " + registerRequest.getCaptcha());
        main.models.User user = new main.models.User();
        user.setIsModerator((byte) 0);
        user.setRegTime(new Date());
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setCode(registerRequest.getCaptchaSecret());
        userRepository.save(user);
        return ResponseEntity.ok(new RegisterResponse(true));
    }

    /**
     * ?????????? ?????????????????? ?????????????????????????????? ????????????.
     */
    private <T extends RegisterResponse> RegisterResponse validateRegisterRequest(RegisterRequest registerRequest) {
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse(false);
        boolean isValidate = true;
        if (registerRequest.getEmail().isEmpty() || registerRequest.getEmail() == null) {
            registerErrorResponse.getErrors().setEmail("E-mail ???? ?????????? ???????? ????????????");
            isValidate = false;
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            registerErrorResponse.getErrors().setEmail("???????? e-mail ?????? ??????????????????????????????");
            isValidate = false;
        }
        if (registerRequest.getName().isEmpty() || registerRequest.getName() == null) {
            registerErrorResponse.getErrors().setName("?????? ???? ?????????? ???????? ????????????");
            isValidate = false;
        }
        if (registerRequest.getPassword().isEmpty() || registerRequest.getPassword() == null) {
            registerErrorResponse.getErrors().setPassword("???????????? ???? ?????????? ???????? ????????????");
            isValidate = false;
        }
        if (registerRequest.getPassword().length() < passwordMinLength) {
            registerErrorResponse.getErrors().setPassword("???????????? ???? ?????????? ???????? ???????????? 6 ????????????????");
            isValidate = false;
        }
        if (registerRequest.getCaptcha().isEmpty() || registerRequest.getCaptcha() == null) {
            registerErrorResponse.getErrors().setCaptcha("?????? ?? ???????????????? ???? ?????????? ???????? ????????????");
            isValidate = false;
            return registerErrorResponse;
        }
        if (captchaCodesRepository.countByCodeAndSecretCode(registerRequest.getCaptcha(),
                registerRequest.getCaptchaSecret()) == 0) {
            registerErrorResponse.getErrors().setCaptcha("?????? ?? ???????????????? ???????????? ???? ??????????");
            isValidate = false;
        }
        if (!isValidate) {
            return registerErrorResponse;
        }
        return new RegisterResponse(true);
    }

}
