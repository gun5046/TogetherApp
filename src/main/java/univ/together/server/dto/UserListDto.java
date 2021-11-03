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
public class UserListDto {
	
	private Long user_idx;
	private String user_nickname;
	private String user_profile;
	
	public UserListDto(User user) {
		this.user_idx = user.getUser_idx();
		this.user_nickname = user.getUser_nickname();
		this.user_profile = EnvironmentVariableConfig.getPhotoUrl() + user.getUser_profile_photo();
	}

}
