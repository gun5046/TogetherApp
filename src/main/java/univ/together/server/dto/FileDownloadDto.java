package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileDownloadDto {
	
	private String file_origin_name;
	
	private String file_hashed_name;
	
	private String file_extension;
	
	private String file_path;
}
