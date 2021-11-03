package univ.together.server.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProfilePhotoDto {

	private String user_profile_photo;
	private MultipartFile photo;
	private Long user_idx;
	private String code;
	
}
