package univ.together.server.model;

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
@Table(name="TEAM_APPLICATION")
@Getter
@Setter(value=AccessLevel.PRIVATE)
public class TeamApplication {
	
	@Id
	@Column(name="TEAM_APPLICATION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long team_application_idx;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name= "USER_IDX")
	private User user_idx;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
}
