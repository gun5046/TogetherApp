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
public class FileReserveDto {
	private Long file_idx; 
	private	Long user_idx;
	
	private LocalDateTime start_datetime;
	
	private LocalDateTime end_datetime;
}
