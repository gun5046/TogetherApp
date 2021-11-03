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
@Table(name = "USER_HOBBY_CAT_BIG")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class UserHobbyCatBig {

	@Id
	@Column(name = "USER_HOBBY_CAT_BIG_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_hobby_cat_big_idx;
	
	@Column(name = "USER_HOBBY_CAT_BIG_NAME")
	private String user_hobby_cat_big_name;
	
}
