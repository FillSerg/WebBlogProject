package main.service;

import main.api.response.AuthCaptchaResponse;
import main.util.CaptchaUtil;
import main.util.DateHelper;
import main.models.CaptchaCode;
import main.repository.CaptchaCodesRepository;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
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

    @Autowired
    public AuthService(PostRepository postRepository, CaptchaCodesRepository captchaCodesRepository) {
        this.postRepository = postRepository;
        this.captchaCodesRepository = captchaCodesRepository;
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
//        log.info("Captcha code is: " + code + ". Secret code is: " + secretCode);
        return ResponseEntity.ok(authCaptchaResponse);
    }

    private void deleteOldCaptcha() {;
        Date date = new Date(new Date().getTime() - (long) captchaDeleteHours * 60 * 60 * 1000);
        List<CaptchaCode> captchaCodes = captchaCodesRepository.findAll();
        for (CaptchaCode captcha : captchaCodes
        ) {
            if (captcha.getTime().before(date)) {
                captchaCodesRepository.delete(captcha);
            }
        }
    }
}
