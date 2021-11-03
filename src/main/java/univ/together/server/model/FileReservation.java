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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FILE_RESERVATION")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class FileReservation {

	@Id
	@Column(name = "FILE_RESERVATION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long file_reservation_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="USER_IDX")
	@JsonIgnore
	private User user_idx;
	
	@Column(name = "RESERVE_START_DATETIME")
	private LocalDateTime reserve_start_datetime;
	
	@Column(name = "RESERVE_END_DATETIME")
	private LocalDateTime reserve_end_datetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_IDX")
	@JsonIgnore
	private File file_idx;
	
}
