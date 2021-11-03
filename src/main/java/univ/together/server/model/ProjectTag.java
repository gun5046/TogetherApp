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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PROJECT_TAG")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class ProjectTag {
	
	@Id
	@Column(name = "PROJECT_TAG_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_tag_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAG_IDX")
	private TagList tag_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TAG_SEARCH_IDX")
	private TagSearch tag_search_idx;
}
