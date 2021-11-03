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
@Table(name = "PROJECT_INVITATION")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class ProjectInvitation {

	@Id
	@Column(name = "PROJECT_INVITATION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_invitation_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@Column(name = "INVITE_DATETIME")
	private LocalDateTime invite_datetime;
	
}
