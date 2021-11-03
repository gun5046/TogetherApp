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
@Table(name = "MATCH_MBTI")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class MatchMbti {

	@Id
	@Column(name = "MATCH_MBTI_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int match_mbti_idx;
	
	@Column(name = "SCORE")
	private int score;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MBTI_1")
	private Mbti mbti_1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MBTI_2")
	private Mbti mbti_2;
	
}
