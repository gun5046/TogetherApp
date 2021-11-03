package univ.together.server.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.SearchMember;
import univ.together.server.model.UserHobbyList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchMemberProfileCardDto {

	private Long user_idx;
	private String user_name;
	private String user_nickname;
	private int age;
	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private String mbti_name;
	private List<String> license = new ArrayList<String>();
	private List<String> hobby_names = new ArrayList<String>();
	private String resume;
	private String comment;
	private String user_profile_photo;
	private Integer num;
	
	public SearchMemberProfileCardDto(SearchMember sm) {
		user_idx = sm.getUser_idx().getUser_idx();
		user_name = sm.getUser_idx().getUser_name();
		user_nickname = sm.getUser_idx().getUser_nickname();
		age = sm.getUser_idx().getUser_age();
		if(sm.getUser_idx().getAddress() != null) {
			main_addr = sm.getUser_idx().getAddress().getMain_addr();
			reference_addr = sm.getUser_idx().getAddress().getReference_addr();
			detail_addr = sm.getUser_idx().getAddress().getDetail_addr();
		}
		if(sm.getUser_idx().getUser_mbti() != null) {
			mbti_name = sm.getUser_idx().getUser_mbti().getMbti_name();
		}
		
		if((sm.getUser_idx().getLicense1() != null) && (!sm.getUser_idx().getLicense1().trim().equals(""))) {
			license.add(sm.getUser_idx().getLicense1());
		}
		if((sm.getUser_idx().getLicense2() != null) && (!sm.getUser_idx().getLicense2().trim().equals(""))) {
			license.add(sm.getUser_idx().getLicense2());
		}
		if((sm.getUser_idx().getLicense3() != null) && (!sm.getUser_idx().getLicense3().trim().equals(""))) {
			license.add(sm.getUser_idx().getLicense3());
		}
		
		for(UserHobbyList uhl : sm.getUser_idx().getUser_hobbies()) {
			if(uhl.getSearch_flag().equals("N")) {
				hobby_names.add(uhl.getHobby_idx().getUser_hobby_cat_small_name());
			}else {
				hobby_names.add(uhl.getHobby_search_idx().getUser_hobby_cat_small_name());
			}
		}
		
		resume = sm.getResume();
		comment = sm.getComment();
		
		user_profile_photo = EnvironmentVariableConfig.getPhotoUrl() + sm.getUser_idx().getUser_profile_photo();
	}
	
}
