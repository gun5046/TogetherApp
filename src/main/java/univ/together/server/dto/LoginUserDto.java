package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {

	private String user_email;
	private String user_pw;
	private String code;
	private String user_name;
	private String user_profile_photo;
	private Long user_idx;
	
}
