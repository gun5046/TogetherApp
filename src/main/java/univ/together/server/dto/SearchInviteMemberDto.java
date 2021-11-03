package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchInviteMemberDto {

	private Long user_idx;
	private Long member_idx;
	private Long project_idx;
	
}
