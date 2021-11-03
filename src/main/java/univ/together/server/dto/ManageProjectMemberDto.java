package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManageProjectMemberDto {

	private Long user_idx;
	private String user_name;
	private String user_nickname;
	private String user_photo;
	private String user_position;
	
	public ManageProjectMemberDto(Member member) {
		this.user_idx = member.getUser_idx().getUser_idx();
		this.user_name = member.getUser_idx().getUser_name();
		this.user_nickname = member.getUser_idx().getUser_nickname();
		this.user_photo = EnvironmentVariableConfig.getPhotoUrl() + member.getUser_idx().getUser_profile_photo();
		this.user_position = member.getMember_right();
	}
	
}
