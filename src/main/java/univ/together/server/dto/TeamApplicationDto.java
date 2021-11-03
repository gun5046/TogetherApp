package univ.together.server.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import univ.together.server.model.TeamApplication;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamApplicationDto {
	private Long team_application_idx;
	private Long user_idx;
	private Long project_idx;
	private String user_name;
	private String user_nickname;
	private String user_mbti;
	private int user_age;
	private Double user_long;
	private Double user_lat;
	public TeamApplicationDto(TeamApplication t){
		this.team_application_idx=t.getTeam_application_idx();
		this.project_idx=t.getProject_idx().getProject_idx();
		this.user_idx=t.getUser_idx().getUser_idx();
		this.user_name = t.getUser_idx().getUser_name();
		this.user_nickname=t.getUser_idx().getUser_nickname();
		this.user_mbti=t.getUser_idx().getUser_mbti().getMbti_name();
		this.user_age=t.getUser_idx().getUser_age();
		this.user_long=t.getUser_idx().getUser_long();
		this.user_lat=t.getUser_idx().getUser_lat();
		
	}
}
