package univ.together.server.dto;

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
public class ChangePwDto {

	private String user_name;
	private String user_email;
	private String user_phone;
	
	@NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 16, message = "아이디는 8 ~ 20글자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 숫자, 문자, 특수문자 각각 1개 이상을 포함해야 합니다.")
	private String user_pw;
	
	@NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 16, message = "아이디는 8 ~ 20글자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}$", message = "비밀번호는 숫자, 문자, 특수문자 각각 1개 이상을 포함해야 합니다.")
	private String user_pw2;
	
}
