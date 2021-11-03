package univ.together.server.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyProjectDetailInfoDto {

	private Long project_idx;
	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	
}
