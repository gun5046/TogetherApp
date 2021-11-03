package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter(value=AccessLevel.PRIVATE)
public class ProjectCard {
	@Id
	@Column(name="PROJECT_CARD_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_card_idx;
	
	@Column(name ="COMMENT")
	private String comment;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="project_idx")
	private Project project_idx;
}
