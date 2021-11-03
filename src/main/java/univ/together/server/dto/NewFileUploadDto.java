package univ.together.server.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewFileUploadDto {
	
	private Long project_idx;
	
	private Long user_idx;
	
	@NotBlank(message = "파일 이름이 없습니다. ")
	private String file_origin_name;
	
	@NotBlank(message = "파일 확장자가 없습니다. ")
	private String file_extension;
	
	@NotBlank
	private String file_type;
	
	@NotBlank
	private MultipartFile multipartfile;
}
