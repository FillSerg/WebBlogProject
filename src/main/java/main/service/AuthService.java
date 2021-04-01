package main.service;

import main.api.response.AuthCaptchaResponse;
import main.base.CaptchaUtil;
import main.base.DateHelper;
import main.models.CaptchaCode;
import main.repository.CaptchaCodesRepository;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

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
        captchaCodes.setTime(DateHelper.getCurrentDate().getTime());
//        deleteOldCaptcha();
        //Сохраняем новую
//        captchaCodesRepository.save(captchaCodes);
//        log.info("Captcha code is: " + code + ". Secret code is: " + secretCode);
        return ResponseEntity.ok(authCaptchaResponse);
//        return ResponseEntity.ok(new AuthCaptchaResponse());
    }

    private void deleteOldCaptcha() {
        Calendar calendar = DateHelper.getCurrentDate();
        calendar.add(Calendar.HOUR, -captchaDeleteHours);
        Date date = new Date();
        //Удаляем устаревшие капчи
//        captchaCodesRepository.deleteOldCaptcha(calendar.getTime());
        captchaCodesRepository.deleteOldCaptcha(date);
    }
}
