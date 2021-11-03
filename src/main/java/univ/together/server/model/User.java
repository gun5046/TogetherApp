package univ.together.server.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter(value=AccessLevel.PRIVATE)
public class User {
	
	@Id
	@Column(name = "USER_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long user_idx;
	
	@Column(name = "USER_NAME")
	private String user_name;
	
	@Column(name = "USER_EMAIL")
	private String user_email;
	
	@Column(name = "USER_PW")
	private String user_pw;
	
	@Column(name = "USER_PHONE")
	private String user_phone;
	
	@Column(name = "USER_NICKNAME")
	private String user_nickname;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_MBTI")
	private Mbti user_mbti;
	
	@Column(name = "USER_BIRTH")
	private LocalDate user_birth;
	
	@Column(name = "USER_AGE")
	private int user_age;
	
	@Column(name = "DELETE_FLAG")
	private String delete_flag;

	@Column(name = "LAST_LOGINED_DATETIME")
	private LocalDateTime last_logined_datetime;
	
	@Column(name = "MODIFIED_DATETIME")
	private LocalDateTime modified_datetime;
	
	@Column(name = "DELETED_DATETIME")
	private LocalDateTime deleted_datetime;
	
	@Column(name = "LICENSE1")
	private String license1;
	
	@Column(name = "LICENSE2")
	private String license2;
	
	@Column(name = "LICENSE3")
	private String license3;
	
	@Column(name = "USER_PROFILE_PHOTO")
	private String user_profile_photo;
	
	@Embedded
	private Address address;
	
	@Column(name = "ENABLE_FLAG")
	private String enable_flag;
	
	@Column(name = "USER_LONG")
	private Double user_long;
	
	@Column(name = "USER_LAT")
	private Double user_lat;
	
	@OneToMany(mappedBy = "user_idx")
	@JsonIgnore
	private List<UserHobbyList> user_hobbies = new ArrayList<UserHobbyList>();
	
	@OneToOne(mappedBy = "user_idx")
	@JsonIgnore
	private SearchMember search_member;
	
	public static User createJoinUser(String user_email, String user_pw, String user_name, 
			String user_nickname, String user_phone, LocalDate user_birth) {
		User user = new User();
		user.setUser_email(user_email);
		user.setUser_pw(user_pw);
		user.setUser_name(user_name);
		user.setUser_nickname(user_nickname);
		user.setUser_phone(user_phone);
		user.setUser_birth(user_birth);
		user.setDelete_flag("N");
		user.setLast_logined_datetime(LocalDateTime.now());
		user.setModified_datetime(LocalDateTime.now());
		user.setEnable_flag("Y");
		user.setUser_profile_photo("basic_picture.png");
		return user;
	}
	
	public int setGetAge() {
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy");
        int this_year = Integer.parseInt(transFormat.format(new Date()));
        int user_year = Integer.parseInt(LocalDate.of(this.user_birth.getYear(), this.user_birth.getMonth(), this.user_birth.getDayOfMonth()).format(DateTimeFormatter.BASIC_ISO_DATE));
        this.user_age = this_year - (user_year / 10000) + 1;
        return this.user_age;
	}
	
}
