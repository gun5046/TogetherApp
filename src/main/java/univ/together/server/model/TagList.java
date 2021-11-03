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
@Table(name = "TAG_LIST")
@Getter
@Setter(value=AccessLevel.PRIVATE)
public class TagList {

	@Id
	@Column(name = "TAG_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tag_idx;
	
	@Column(name = "TAG_NAME")
	private String tag_name;
	
	@Column(name = "TAG_DETAIL_NAME")
	private String tag_detail_name;
	
}
