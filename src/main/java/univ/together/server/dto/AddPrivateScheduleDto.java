package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPrivateScheduleDto {

	private String schedule_name;
	private String schedule_content;
	private LocalDateTime schedule_start_datetime;
	private LocalDateTime schedule_end_datetime;
	private Long writer_idx;
	
}
