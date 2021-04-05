package main.repository;

import main.models.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CaptchaCodesRepository extends JpaRepository<CaptchaCode, Integer> {
    int countByCodeAndSecretCode(String code, String secretCode);

//    @Transactional
   /* @Modifying
    @Query("delete from CaptchaCode where CaptchaCode.time < :deleteDate")
    void deleteOldCaptcha(@Param("deleteDate") Date deleteDate);*/


}
