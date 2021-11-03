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
@Table(name = "PRIVATE_SCHEDULE")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class PrivateSchedule {

	@Id
	@Column(name = "PRIVATE_SCHEDULE_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long private_schedule_idx;
	
	@Column(name = "TITLE")
	private String title;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@Column(name = "BODY")
	private String body;
	
	@Column(name = "START_DATETIME")
	private LocalDateTime start_datetime;
	
	@Column(name = "END_DATETIME")
	private LocalDateTime end_datetime;
	
}
