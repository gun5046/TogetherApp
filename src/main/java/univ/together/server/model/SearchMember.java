package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SEARCH_MEMBER")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class SearchMember {
	
	@Id
	@Column(name = "SEARCH_MEMBER_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long search_member_idx;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@Column(name = "RESUME")
	private String resume;
	
	@Column(name = "COMMENT")
	private String comment;

}
