package univ.together.server.dto;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
	
	@NotBlank
	@Size(min = 2, max = 20, message = "프로젝트 이름은 2~20자리 글자 사이여야 합니다.")
	private String project_name;
	private Long user_idx;
	private LocalDate start_date;
	

	private LocalDate end_date;
	
	
	@NotBlank
	private String professionality; //전문성 High, Mid, Low , Any
	
	@Size(max = 300, message = "300자 이내로 작성해주세요.")
	@NotBlank
	private String project_exp; // 세부내용
	
	@NotBlank
	private String project_type; //대외, 스터디 .. Study, Out, in, Any
	
	private int tag_num;
	
	private String tag_name[] = new String[tag_num];
	
	private String detail_name[] = new String[tag_num];
	// 대분류 기타 소분류 기타일때  -   게임(롤 배그 스타 etc-> 기타) 낚시 
	
}

