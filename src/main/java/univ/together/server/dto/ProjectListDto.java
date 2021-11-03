package univ.together.server.dto;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.Member;
import univ.together.server.model.Project;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListDto {

	private Long project_idx;
	private String project_name;
	private int count;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private int file_num;
	private List<String> user_profile_photoes = new ArrayList<String>();

	public ProjectListDto(Member member) {
		this.project_idx = member.getProject_idx().getProject_idx();
		this.project_name = member.getProject_idx().getProject_name();
		this.count = member.getProject_idx().getMember_num();
		this.project_exp = member.getProject_idx().getProject_exp();
		this.start_date = member.getProject_idx().getStart_date();
		this.end_date = member.getProject_idx().getEnd_date();
		this.file_num = member.getProject_idx().getFiles().size();
		Project project = member.getProject_idx();
		for(Member members : project.getMembers()) {
			if(!(members.getUser_idx().getUser_profile_photo() == null || members.getUser_idx().getUser_profile_photo().equals("null"))) {
				user_profile_photoes.add(EnvironmentVariableConfig.getPhotoUrl() + members.getUser_idx().getUser_profile_photo());
			}else {
				user_profile_photoes.add(null);
			}
		}
	}

}
