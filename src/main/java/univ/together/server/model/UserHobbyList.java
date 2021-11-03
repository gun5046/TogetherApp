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
@Table(name = "USER_HOBBY_LIST")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class UserHobbyList {

	@Id
	@Column(name = "USER_HOBBY_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_hobby_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOBBY_IDX")
	private UserHobbyCatSmall hobby_idx;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HOBBY_SEARCH_IDX")
	private UserHobbySearch hobby_search_idx;
	
	@Column(name = "SEARCH_FLAG")
	private String search_flag;
	
}
