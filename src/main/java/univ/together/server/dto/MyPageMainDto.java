package univ.together.server.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageMainDto {
	
	private String user_name;
	private String user_nickname;
	private String user_profile_photo;
	
	public MyPageMainDto(User user) {
		this.user_name = user.getUser_name();
		this.user_nickname = user.getUser_nickname();
		
		if(user.getUser_profile_photo() != null) {
			this.user_profile_photo = EnvironmentVariableConfig.getPhotoUrl() + user.getUser_profile_photo();
		}
		
	}

}