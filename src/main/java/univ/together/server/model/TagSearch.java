package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TAG_SEARCH")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class TagSearch {
	
	@Id
	@Column(name = "TAG_SEARCH_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tag_search_idx;
	
	@Column(name = "SEARCH_NAME")
	private String search_name;
	
	@Column(name = "SEARCH_DETAIL_NAME")
	private String search_detail_name;

}
