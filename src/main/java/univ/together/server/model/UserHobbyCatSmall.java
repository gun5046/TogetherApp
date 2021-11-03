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
@Table(name = "USER_HOBBY_CAT_SMALL")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class UserHobbyCatSmall {

	@Id
	@Column(name = "USER_HOBBY_CAT_SMALL_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_hobby_cat_small_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_HOBBY_CAT_BIG_IDX")
	private UserHobbyCatBig user_hobby_cat_big_idx;
	
	@Column(name = "USER_HOBBY_CAT_SMALL_NAME")
	private String user_hobby_cat_small_name;
	
}
