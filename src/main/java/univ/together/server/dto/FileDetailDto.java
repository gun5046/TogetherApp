package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDetailDto {
	private String version_user_name;
	private LocalDateTime file_modified_datetime;
	private String file_modified_comment;

	private String temp_delete_member_name;
	private String temp_delete_flag;
	
	private Long reserve_user_idx;
	private String reserve_user_name;
	private LocalDateTime reserve_start_datetime;
	private LocalDateTime reserve_end_datetime;
	private int file_reservation_flag;
	private String file_type;
}
