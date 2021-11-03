package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.PrivateSchedule;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivateScheduleListDto {

	private String title;
	private String body;
	private LocalDateTime start_datetime;
	private LocalDateTime end_datetime;
	
	public PrivateScheduleListDto(PrivateSchedule privateSchedule) {
		if(privateSchedule != null) {
			this.title = privateSchedule.getTitle();
			if(privateSchedule.getBody() != null) this.body = privateSchedule.getBody();
			this.start_datetime = privateSchedule.getStart_datetime();
			this.end_datetime = privateSchedule.getEnd_datetime();
		}
	}
	
}
