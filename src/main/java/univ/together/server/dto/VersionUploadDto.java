package univ.together.server.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VersionUploadDto {
	
	private Long user_modified_idx;
	
	private Long file_idx;
	
	private String file_modified_comment;
	
	private MultipartFile multipartfile;
	
}
