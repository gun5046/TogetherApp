package univ.together.server.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

	private String user_name;
	private String user_nickname;
	private String user_profile_photo;
	
	private String user_email;
	private String user_phone;
	private LocalDate user_birth;
	private int user_age;
	private String license1;
	private String license2;
	private String license3;
	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private String post_num;
	private String user_mbti_name;
	
	private List<String> user_hobbies = new ArrayList<String>();
	private List<Long> user_hobby_idxes = new ArrayList<Long>();
	private List<Long> hobby_idxes = new ArrayList<Long>();
	private List<Long> hobby_search_idxes = new ArrayList<Long>();
	
	public UserProfileDto(User user) {
		this.user_name = user.getUser_name();
		this.user_nickname = user.getUser_nickname();
		
		if(user.getUser_profile_photo() != null) {
			this.user_profile_photo = EnvironmentVariableConfig.getPhotoUrl() + user.getUser_profile_photo();
		}
		
		if(user.getAddress() != null) {
			if(user.getAddress().getMain_addr() != null) {
				this.main_addr = user.getAddress().getMain_addr();
			}
			
			if(user.getAddress().getReference_addr() != null) {
				this.reference_addr = user.getAddress().getReference_addr();
			}
			
			if(user.getAddress().getDetail_addr() != null) {
				this.detail_addr = user.getAddress().getDetail_addr();
			}
			
			if(user.getAddress().getPost_num() != null) {
				this.post_num = user.getAddress().getPost_num();
			}
		}
		
		this.user_email = user.getUser_email();
		this.user_phone = user.getUser_phone();
		this.user_birth = user.getUser_birth();
		this.user_age = user.getUser_age();
		
		if(user.getLicense1() != null) {
			this.license1 = user.getLicense1();
		}
		if(user.getLicense2() != null) {
			this.license2 = user.getLicense2();
		}
		if(user.getLicense3() != null) {
			this.license3 = user.getLicense3();
		}
		if(user.getUser_mbti() != null) {
			this.user_mbti_name = user.getUser_mbti().getMbti_name();
		}

		for(int i = 0; i < user.getUser_hobbies().size(); i++) {
			String search_flag = user.getUser_hobbies().get(i).getSearch_flag();
			if(search_flag.equals("N")) {
				user_hobbies.add(user.getUser_hobbies().get(i).getHobby_idx().getUser_hobby_cat_small_name());
				user_hobby_idxes.add(user.getUser_hobbies().get(i).getUser_hobby_idx());
				hobby_idxes.add(user.getUser_hobbies().get(i).getHobby_idx().getUser_hobby_cat_small_idx());
				hobby_search_idxes.add(null);
			}else {
				user_hobbies.add(user.getUser_hobbies().get(i).getHobby_search_idx().getUser_hobby_cat_small_name());
				user_hobby_idxes.add(user.getUser_hobbies().get(i).getUser_hobby_idx());
				hobby_search_idxes.add(user.getUser_hobbies().get(i).getHobby_search_idx().getUser_hobby_search_idx());
				hobby_idxes.add(null);
			}
		}
	}
	
}