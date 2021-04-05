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
        //Удаление < 1 час
        deleteOldCaptcha();
        //Сохранение новой
        captchaCodesRepository.save(captchaCodes);
        log.info("Captcha code: " + code + ". Secret code: " + secretCode);
        return ResponseEntity.ok(authCaptchaResponse);
    }

    private void deleteOldCaptcha() {
        ;
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
    public Object register(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = validateRegisterRequest(registerRequest);

        if (registerResponse instanceof RegisterErrorResponse) {
            return ResponseEntity.ok(registerResponse);
        }
        log.info(registerRequest.getName() + " / " + registerRequest.getPassword() +
                " / " + registerRequest.getCaptcha());
        main.models.User user = new main.models.User();
        user.setIsModerator((byte) 0);
//            user.setRegTime(DateHelper.getCurrentDate().getTime());
        user.setRegTime(new Date());
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
            /*user.setPassword(SecurityConfig
                    .passwordEncoder()
                    .encode(registerRequest.getPassword()));*/
        user.setPassword(registerRequest.getPassword());
        user.setCode(registerRequest.getCaptchaSecret());

        userRepository.save(user);
        return ResponseEntity.ok(new RegisterResponse(true));
    }

    /**
     * Метод валидации регистрационных данных.
     */
    private <T extends RegisterResponse> RegisterResponse validateRegisterRequest(RegisterRequest registerRequest) {
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse(false);
        //Если регистрация закрыта
       /* if (!SettingsService.getSettingsValue(globalSettingsRepository, "MULTIUSER_MODE")) {
            throw new RegistrationClosedException("Register is closed");
        }*/
        boolean isValidate = true;
        if (registerRequest.getEmail().isEmpty() || registerRequest.getEmail() == null) {
            registerErrorResponse.setEmail("E-mail не может быть пустым");
            isValidate = false;
        }
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            registerErrorResponse.setEmail("Этот e-mail уже зарегистрирован");
            isValidate = false;
        }
        if (registerRequest.getName().isEmpty() || registerRequest.getName() == null) {
            registerErrorResponse.setName("Имя не может быть пустым");
            isValidate = false;
        }
        if (registerRequest.getPassword().isEmpty() || registerRequest.getPassword() == null) {
            registerErrorResponse.setPassword("Пароль не может быть пустым");
            isValidate = false;
        }
        if (registerRequest.getPassword().length() < passwordMinLength) {
            registerErrorResponse.setPassword("Пароль не может быть короче 6 символов");
            isValidate = false;
        }
        if (registerRequest.getCaptcha().isEmpty() || registerRequest.getCaptcha() == null) {
            registerErrorResponse.setCaptcha("Код с картинки не может быть пустым");
            isValidate = false;
            return registerErrorResponse;
        }
        if (captchaCodesRepository.countByCodeAndSecretCode(registerRequest.getCaptcha(),
                registerRequest.getCaptchaSecret()) == 0) {
            registerErrorResponse.setCaptcha("Код с картинки указан не верно");
            isValidate = false;
        }
        if (!isValidate){
            return registerErrorResponse;
        }
        return new RegisterResponse(true);
    }

}
