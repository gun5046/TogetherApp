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
@Table(name = "PROJECT_SCHEDULE")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class ProjectSchedule {

	@Id
	@Column(name = "SCHEDULE_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long schedule_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
	
	@Column(name = "SCHEDULE_NAME")
	private String schedule_name;
	
	@Column(name = "SCHEDULE_CONTENT")
	private String schedule_content;
	
	@Column(name = "SCHEDULE_START_DATETIME")
	private LocalDateTime schedule_start_datetime;
	
	@Column(name = "SCHEDULE_END_DATETIME")
	private LocalDateTime schedule_end_datetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WRITER_IDX")
	private User writer_idx;
	
}
