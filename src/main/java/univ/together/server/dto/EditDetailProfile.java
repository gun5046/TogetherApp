package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditDetailProfile {

	private Long user_idx;
	private String flag; // birth, license, mbti
	private String value;
	/*
	 * birth: 1997-10-26
	 * license: 정보처리기사,정보보안기사,null
	 * mbti: 1 --> String으로
	 * 
	 */
	
}
