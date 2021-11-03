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
@Table(name = "USER_HOBBY_SEARCH")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class UserHobbySearch {

	@Id
	@Column(name = "USER_HOBBY_SEARCH_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_hobby_search_idx;
	
	@Column(name = "COUNT")
	private Integer count;
	
	@Column(name = "USER_HOBBY_CAT_BIG_IDX")
	private Long user_hobby_cat_big_idx;
	
	@Column(name = "USER_HOBBY_CAT_BIG_NAME")
	private String user_hobby_cat_big_name;
	
	@Column(name = "USER_HOBBY_CAT_SMALL_NAME")
	private String user_hobby_cat_small_name;
	
}
