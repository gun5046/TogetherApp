package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.FileReservation;

@Getter
@Setter
@NoArgsConstructor
public class GetReservationListDto {
	
	private Long file_reservation_idx;
	private LocalDateTime start_datetime;
	private LocalDateTime end_datetime;
	private String user_name;
	private Long user_idx;
	
	public GetReservationListDto(FileReservation fr) {
		this.file_reservation_idx = fr.getFile_reservation_idx();
		this.start_datetime = fr.getReserve_start_datetime();
		this.end_datetime = fr.getReserve_end_datetime();
		this.user_name = fr.getUser_idx().getUser_name();
		this.user_idx = fr.getUser_idx().getUser_idx();
	}
	
}
