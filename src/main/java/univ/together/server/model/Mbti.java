package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value=AccessLevel.PRIVATE)
@Entity
public class Mbti {

	@Id
	@Column(name = "MBTI_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mbti_idx;
	
	@Column(name = "MBTI_NAME")
	private String mbti_name;
	
}
