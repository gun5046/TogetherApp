package univ.together.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_VALIDATION")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class UserValidation {

	@Id
	@Column(name = "VALIDATION_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long validation_idx;
	
	@Column(name = "VALIDATION_FLAG")
	private String validation_flag;
	
	@Column(name = "SENT_DATETIME")
	private LocalDateTime sent_datetime;
	
	@Column(name = "PERMIT_DATETIME")
	private LocalDateTime permit_datetime;
	
	@Column(name = "USER_DEVICE")
	private String user_device;
	
	@Column(name = "VALIDATION_VALUE")
	private String validation_value;
	
	// 기본적인 인증 값을 생성하는 기능(기본 값 = email)
	public static UserValidation createUserValidation(String user_device, String validation_value) {
		UserValidation userValidation = new UserValidation();
		userValidation.setValidation_flag("E");
		userValidation.setSent_datetime(LocalDateTime.now());
		userValidation.setPermit_datetime(LocalDateTime.now().plusMinutes(5));
		userValidation.setUser_device(user_device);
		userValidation.setValidation_value(validation_value);
		return userValidation;
	}
	
	// 인증 타입을 변경(email --> phone)
	public void changeMode() {
		this.validation_flag = "P";
	}
	
}
