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
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FILE_VERSION")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class FileVersion {

	@Id
	@Column(name = "FILE_VERSION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long file_version_idx;
	
	@Column(name = "FILE_MODIFIED_DATETIME")
	private LocalDateTime file_modified_datetime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_MODIFIED_MEMBER_IDX")
	private User file_modified_member_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_IDX")
	private File file_idx;
	
	@Column(name = "FILE_MODIFIED_COMMENT")
	private String file_modified_comment;
	
}
