package univ.together.server.validator;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import univ.together.server.dto.JoinUserDto;

public class JoinValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return JoinUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JoinUserDto joinUserDto = (JoinUserDto) target;

//        if(!joinUserDto.isUserNicknameExist()) {
//            errors.rejectValue("user_nickname", "notDupNickCheck", "닉네임 중복 체크를 해주세요.");
//        }
//
//        if(!joinUserDto.isUserEmailExist()) {
//            errors.rejectValue("user_email", "notDupEmailCheck", "이메일 중복 체크를 해주세요.");
//        }
//        if(!joinUserDto.isUserEmailExist()) {
//        	errors.rejectValue("user_email", "notDupEmailCheck", "이메일 중복 체크를 해주세요.");
//        }

        joinUserDto.setUser_birth_string(joinUserDto.getUser_birth().format(DateTimeFormatter.BASIC_ISO_DATE));
        if(joinUserDto.getUser_birth_string().length() == 8) {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy");
            int month = Integer.parseInt(joinUserDto.getUser_birth_string().substring(4, 6));
            int this_year = Integer.parseInt(transFormat.format(new Date()));
            int user_year = Integer.parseInt(joinUserDto.getUser_birth_string().substring(0, 4));
            int day = Integer.parseInt(joinUserDto.getUser_birth_string().substring(6, 8));
            int age = this_year - user_year + 1;

            if (age >= 101 || age <= 15) {
                errors.rejectValue("user_birth_string", "notProperYear", "16살 ~ 100살만 입력 가능합니다.");
            }

            if (month <= 0 || month >= 13) {
                errors.rejectValue("user_birth_string", "notProperMonth", "월을 확인해주세요.");
            }

            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day <= 0 || day >= 31) errors.rejectValue("user_birth_string", "notProperDay", "일을 확인해주세요.");
            } else if (month == 2) {
                if (((user_year % 4 == 0) && (user_year % 100 != 0)) || (user_year % 400 == 0)) {
                    if (day <= 0 || day >= 30) errors.rejectValue("user_birth_string", "notProperDay", "일을 확인해주세요.");
                } else {
                    if (day <= 0 || day >= 29) errors.rejectValue("user_birth_string", "notProperDay", "일을 확인해주세요.");
                }
            } else {
                if (day <= 0 || day >= 30) errors.rejectValue("user_birth_string", "notProperDay", "일을 확인해주세요.");
            }
        }
    }
}