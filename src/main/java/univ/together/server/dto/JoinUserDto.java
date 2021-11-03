package univ.together.server.dto;


import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserDto {

	@NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식을 지켜주세요.")
	private String user_email;
	
	@NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 16, message = "아이디는 8 ~ 20글자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 숫자, 문자, 특수문자 각각 1개 이상을 포함해야 합니다.")
	private String user_pw;
	
	@NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 10, message = "이름은 2 ~ 10글자 사이여야 합니다.")
    @Pattern(regexp = "[가-힣]*", message = "이름은 한글만 입력 가능합니다.")
	private String user_name;
	
	@NotBlank(message = "전화 번호를 입력하세요")
	@Size(min = 12, max = 13, message = "전화번호의 길이를 확인하세요.")
	private String user_phone;
	
	@NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 10, message = "이름은 2 ~ 10글자 사이여야 합니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣]*", message = "닉네임은 영문 대소문자, 숫자, 한글만 입력 가능합니다.")
	private String user_nickname;
	
	
	private LocalDate user_birth;
	private int user_age;
	private String user_birth_string;
	
}
