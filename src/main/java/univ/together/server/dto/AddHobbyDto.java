package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddHobbyDto {

	private Long user_idx;
	private Long big_idx;
	private Long small_idx;
	private String big_name;
	private String small_name;
	
}
