package univ.together.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
public class File {

	@Id
	@Column(name = "FILE_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long file_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
	
	@Column(name = "FILE_ORIGIN_NAME")
	private String file_origin_name;
	
	@Column(name = "FILE_HASHED_NAME")
	private String file_hashed_name;
	
	@Column(name = "FILE_EXTENSION")
	private String file_extension;
	
	@Column(name = "FILE_TYPE")
	private String file_type;
	
	@Column(name = "TEMP_DELETE_FLAG")
	private String temp_delete_flag;
	
	@Column(name = "COM_DELETE_FLAG")
	private String com_delete_flag;
	
	@Column(name = "TEMP_DELETE_DATETIME")
	private LocalDateTime temp_delete_datetime;
	
	@Column(name = "COM_DELETE_DATETIME")
	private LocalDateTime com_delete_datetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TEMP_DELETE_MEMBER_IDX")
	private User temp_delete_member_idx;
	
	@Column(name = "FILE_UPLOAD_DATETIME")
	private LocalDateTime file_upload_datetime;
	
	@Column(name = "FILE_PATH")
	private String file_path;
	
	@Column(name = "FILE_SEMA_FLAG")
	private String file_sema_flag;
	
	@Builder
	public File(String file_origin_name, String file_hashed_name, String file_extension, String file_type, String file_path) {
		
		this.file_origin_name=file_origin_name;
		this.file_hashed_name=file_hashed_name;
		this.file_extension=file_extension;
		this.file_type=file_type;
		this.file_upload_datetime=LocalDateTime.now();
		this.file_path=file_path;
		this.file_sema_flag="1";
	}
}
