package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.ProjectSchedule;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailScheduleDto {

	private String schedule_title;
	private LocalDateTime schedule_start_datetime;
	private LocalDateTime schedule_end_datetime;
	private String schedule_content;
	private String schedule_writer_name;
	private String schedule_writer_nickname;
	
	public ProjectDetailScheduleDto(ProjectSchedule projectSchedule) {
		this.schedule_title = projectSchedule.getSchedule_name();
		if(projectSchedule.getSchedule_content() != null) this.schedule_content = projectSchedule.getSchedule_content();
		this.schedule_start_datetime = projectSchedule.getSchedule_start_datetime();
		this.schedule_end_datetime = projectSchedule.getSchedule_end_datetime();
		this.schedule_writer_name = projectSchedule.getWriter_idx().getUser_name();
		this.schedule_writer_nickname = projectSchedule.getWriter_idx().getUser_nickname();
	}
	
}
