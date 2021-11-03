package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.ProjectSchedule;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleDto {

	private Long schedule_idx;
	private Long project_idx;
	private String schedule_name;
	private String schedule_content;
	private LocalDateTime schedule_start_datetime;
	private LocalDateTime schedule_end_datetime;
	private Long writer_idx;
	private String writer_profile_photo;
	
	public ProjectScheduleDto(ProjectSchedule projectSchedule) {
		this.schedule_idx = projectSchedule.getSchedule_idx();
		this.project_idx = projectSchedule.getProject_idx().getProject_idx();
		this.schedule_name = projectSchedule.getSchedule_name();
		this.schedule_content = projectSchedule.getSchedule_content();
		this.schedule_start_datetime = projectSchedule.getSchedule_start_datetime();
		this.schedule_end_datetime = projectSchedule.getSchedule_end_datetime();
		this.writer_idx = projectSchedule.getWriter_idx().getUser_idx();
		this.writer_profile_photo = EnvironmentVariableConfig.getPhotoUrl() + projectSchedule.getWriter_idx().getUser_profile_photo();
	}
	
}
