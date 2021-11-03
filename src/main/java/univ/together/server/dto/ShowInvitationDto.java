package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.ProjectInvitation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowInvitationDto {

	private Long project_idx;
	private String project_name;
	private String project_exp;
	private int member_num;
	private LocalDateTime invite_time;
	
	public ShowInvitationDto(ProjectInvitation projectInvitation) {
		this.project_idx = projectInvitation.getProject_idx().getProject_idx();
		this.project_name = projectInvitation.getProject_idx().getProject_name();
		this.project_exp = projectInvitation.getProject_idx().getProject_exp();
		this.member_num = projectInvitation.getProject_idx().getMember_num();
		this.invite_time = projectInvitation.getInvite_datetime();
	}
	
}
