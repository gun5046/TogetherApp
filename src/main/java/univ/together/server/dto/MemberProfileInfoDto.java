package univ.together.server.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.User;
import univ.together.server.model.UserHobbyList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileInfoDto {
	
	private String user_name;
	private String user_nickname;
	private int age;
	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private String mbti_name;
	private List<String> license = new ArrayList<String>();
	private List<String> hobby_names = new ArrayList<String>();
	private String user_profile_photo;
	
	public MemberProfileInfoDto(User user) {
		user_name = user.getUser_name();
		user_nickname = user.getUser_nickname();
		age = user.setGetAge();
		if(user.getAddress() != null) {
			main_addr = user.getAddress().getMain_addr();
			reference_addr = user.getAddress().getReference_addr();
			detail_addr = user.getAddress().getDetail_addr();
		}
		if(user.getUser_mbti() != null) mbti_name = user.getUser_mbti().getMbti_name();

		
		if((user.getLicense1() != null) && (!user.getLicense1().trim().equals(""))) {
			license.add(user.getLicense1());
		}
		if((user.getLicense2() != null) && (!user.getLicense2().trim().equals(""))) {
			license.add(user.getLicense2());
		}
		if((user.getLicense3() != null) && (!user.getLicense3().trim().equals(""))) {
			license.add(user.getLicense3());
		}
		
		for(UserHobbyList uhl : user.getUser_hobbies()) {
			if(uhl.getSearch_flag().equals("N")) {
				hobby_names.add(uhl.getHobby_idx().getUser_hobby_cat_small_name());
			}else {
				hobby_names.add(uhl.getHobby_search_idx().getUser_hobby_cat_small_name());
			}
		}
		user_profile_photo = EnvironmentVariableConfig.getPhotoUrl() + user.getUser_profile_photo();
	}

}
