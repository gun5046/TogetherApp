package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SearchCondition {

	@Id
	@Column(name = "SEARCH_CONDITION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long search_condition_idx;
	
	@Column(name = "MIN_AGE")
	private int min_age;
	
	@Column(name = "MAX_AGE")
	private int max_age;
	
	@Column(name = "LICENSE1")
	private String license1;
	
	@Column(name = "LICENSE2")
	private String license2;
	
	@Column(name = "LICENSE3")
	private String license3;
	
	@Column(name = "MAIN_ADDR")
	private String main_addr;
	
	@Column(name = "REFERENCE_ADDR")
	private String reference_addr;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@Column(name = "HOBBY_SMALL_IDX1")
	private String hobby_small_idx1;
	
	@Column(name = "HOBBY_SMALL_IDX2")
	private String hobby_small_idx2;
	
	@Column(name = "HOBBY_SMALL_IDX3")
	private String hobby_small_idx3;
	
}
