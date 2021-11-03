package univ.together.server.dto;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

import lombok.Setter;
import univ.together.server.model.UserHobbyList;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddHobbyReturnDto {

	private Long user_hobby_idxes;
	private Long hobby_idxes;
	private Long hobby_search_idxes;
	
	public AddHobbyReturnDto(UserHobbyList userHobbyList) {
		this.user_hobby_idxes = userHobbyList.getUser_hobby_idx();
		try {
			this.hobby_idxes = userHobbyList.getHobby_idx().getUser_hobby_cat_small_idx();
		}catch(Exception e) {
			this.hobby_idxes = null;
		}
		try {
			this.hobby_search_idxes = userHobbyList.getHobby_search_idx().getUser_hobby_search_idx();
		}catch(Exception e) {
			this.hobby_search_idxes = null;
		}
		
	}
	
}
