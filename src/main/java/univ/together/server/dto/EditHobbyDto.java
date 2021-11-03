package univ.together.server.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.UserHobbyCatSmall;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditHobbyDto {

	
	private Map<String, String> hobby_idx = new HashMap<String, String>();
	private Map<String, String> hobby_name = new HashMap<String, String>();
	
	public EditHobbyDto(UserHobbyCatSmall uhcs) {
		hobby_idx.put(String.valueOf(uhcs.getUser_hobby_cat_big_idx().getUser_hobby_cat_big_idx()), String.valueOf(uhcs.getUser_hobby_cat_small_idx()));
		hobby_name.put(uhcs.getUser_hobby_cat_big_idx().getUser_hobby_cat_big_name(), uhcs.getUser_hobby_cat_small_name());
	}
	
}
