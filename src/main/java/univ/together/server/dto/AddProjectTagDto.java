package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProjectTagDto {

	private Long tag_idx;
	private String tag_name;
	private String tag_detail_name;
	
}
