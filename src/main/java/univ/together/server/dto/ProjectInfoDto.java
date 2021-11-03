package univ.together.server.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.Member;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectSchedule;
import univ.together.server.model.ProjectTag;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoDto {

	private Long project_idx;
	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private int member_num;
	
	private List<String> tag_names = new ArrayList<String>();
	
	private List<Long> user_idxes = new ArrayList<Long>();
	private List<String> user_names = new ArrayList<String>();
	
	private List<String> schedule_names = new ArrayList<String>();
	private List<String> schedule_contents = new ArrayList<String>();
	private List<LocalDateTime> schedule_start_datetimes = new ArrayList<LocalDateTime>();
	private List<LocalDateTime> schedule_end_datetimes = new ArrayList<LocalDateTime>();
	private List<String> schedule_writer_names = new ArrayList<String>();
	
	private String code;
	
	public ProjectInfoDto(Project project) {
		this.project_idx = project.getProject_idx();
		this.project_name = project.getProject_name();
		this.project_exp = project.getProject_exp();
		this.start_date = project.getStart_date();
		this.end_date = project.getEnd_date();
		this.professionality = project.getProfessionality();
		this.project_type = project.getProject_type();
		this.member_num = project.getMember_num();
		for (ProjectTag tags : project.getTags()) {
			if(!tags.getTag_idx().getTag_name().equals("기타")) tag_names.add(tags.getTag_idx().getTag_name());
			else tag_names.add(tags.getTag_idx().getTag_detail_name());
		}
		for(Member members : project.getMembers()) {
			user_idxes.add(members.getUser_idx().getUser_idx());
			user_names.add(members.getUser_idx().getUser_name());
		}
		for(ProjectSchedule projectSchedules : project.getProjectSchedules()) {
			schedule_names.add(projectSchedules.getSchedule_name());
			schedule_contents.add(projectSchedules.getSchedule_content());
			schedule_start_datetimes.add(projectSchedules.getSchedule_start_datetime());
			schedule_end_datetimes.add(projectSchedules.getSchedule_end_datetime());
			schedule_writer_names.add(projectSchedules.getWriter_idx().getUser_nickname());
		}
	}
	
}
