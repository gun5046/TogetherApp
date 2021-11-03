package univ.together.server.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import univ.together.server.configuration.EnvironmentVariableConfig;
import univ.together.server.dto.AddHobbyDto;
import univ.together.server.dto.AddHobbyReturnDto;
import univ.together.server.dto.AddPrivateScheduleDto;
import univ.together.server.dto.ChangeProfilePhotoDto;
import univ.together.server.dto.ChangePwDto;
import univ.together.server.dto.CheckUserInfoForChangePwDto;
import univ.together.server.dto.CheckUserInfoForFindIdDto;
import univ.together.server.dto.DecideJoinProjectDto;
import univ.together.server.dto.EditDetailProfile;
import univ.together.server.dto.EditHobbyDto;
import univ.together.server.dto.EditUserAddressDto;
import univ.together.server.dto.FindIdDto;
import univ.together.server.dto.JoinUserDto;
import univ.together.server.dto.LoginUserDto;
import univ.together.server.dto.MyPageMainDto;
import univ.together.server.dto.PrivateScheduleListDto;
import univ.together.server.dto.ShowInvitationDto;
import univ.together.server.dto.UserListDto;
import univ.together.server.dto.UserProfileDto;
import univ.together.server.model.User;
import univ.together.server.model.UserValidation;
import univ.together.server.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailUtil mailSender;
	
	@Transactional
	public void login(String user_email, String user_pw, LoginUserDto loginUserDto) {
		try {
			String pw = userRepository.getPwByEmail(user_email);
			if(passwordEncoder.matches(user_pw, pw) == true) {
				Long user_idx = userRepository.getUserIdxByEmail(user_email);
				userRepository.changeLastLoginedDatetime(user_idx);
				loginUserDto.setUser_name(userRepository.getUserNameByIdx(user_idx));
				loginUserDto.setUser_profile_photo(userRepository.getUserProfilePhoto(user_idx));
				loginUserDto.setCode("success");
				loginUserDto.setUser_idx(user_idx);
			}
			else {
				loginUserDto.setCode("wrong_pw");
				loginUserDto.setUser_idx(0L);
			}
		} catch(Exception e) {
			loginUserDto.setCode("not_user");
			loginUserDto.setUser_idx(0L);
		}
		loginUserDto.setUser_email("");
		loginUserDto.setUser_pw("");
		loginUserDto.setUser_profile_photo(EnvironmentVariableConfig.getPhotoUrl() + loginUserDto.getUser_profile_photo());
	}
	
	@Transactional
	public void join(JoinUserDto joinUserDto) {
	
		String raw_phone = joinUserDto.getUser_phone();
		int pos1 = raw_phone.indexOf("-");
		int pos2 = raw_phone.lastIndexOf("-");
		joinUserDto.setUser_pw(passwordEncoder.encode(joinUserDto.getUser_pw()));
		User user = User.createJoinUser(joinUserDto.getUser_email(), joinUserDto.getUser_pw(), 
				joinUserDto.getUser_name(), joinUserDto.getUser_nickname(), 
				joinUserDto.getUser_phone(), joinUserDto.getUser_birth());
		user.setGetAge();
		userRepository.joinUser(user);
		userRepository.initUserMbti(joinUserDto.getUser_name(), joinUserDto.getUser_nickname());
	}
	
	// 이메일 중복검사와 간단한 형식 검사
	public String checkEmail(String user_email) {
		
		// 플러터로 반환할 값(상태 플래그)
		String code = "";
		
		// 혹시 모를 공백 제거
		user_email = user_email.trim();
		
		// 이메일이 공백인지 확인
		if(user_email.length() == 0) code = "length_error";
		
		// 이메일 형식인지 간단하게 확인
		if(!user_email.contains("@")) code = "not_email";
		
		
		// 상태 플래그가 그대로이면 이메일이 중복되는지 확인
		if(code.trim().equals("")) {
			try {
				String origin_user_email = userRepository.checkEmail(user_email);
				code = origin_user_email == null ? "permit" : "duplication";
			}catch(Exception e) {
				code = "permit";
			}
		}
		
		return code;
		
	}
	
	// 이메일이 본인 소유인지 확인을 위해 메시지를 보냄
	@Transactional
	public void sendMail(String user_email) {
		
		// 해시화된 인증 값
		String hashed_code = passwordEncoder.encode(user_email + System.currentTimeMillis());
		
		// 해시화된 인증 값을 유저의 이메일로 보냄
		mailSender.sendEmail(user_email, "이메일 본인 인증", hashed_code);
		
		
		// 이 인증을 위한 값을 DB에 저장 Start
		
		UserValidation validation = UserValidation.createUserValidation(user_email, hashed_code);
		userRepository.saveValidation(validation);
		
		// 인증을 위한 값을 DB에 저장 End
		
	}
	
	// 휴대폰 중복 검사와 간단한 형식 검사
	public String checkPhone(String user_phone) {
		
		// 플러터로 반환할 값(상태 플래그)
		String code = "";
		
		// 혹시 모를 공백 제거
		user_phone = user_phone.trim();
		
		// 핸드폰 번호의 형식을 검사할 정규식
		String pattern = "^[0-9]*$";
		
		// 휴대폰 번호 길이 검사('-'를 포함하여 12 ~ 13자리)
		if(!(user_phone.length() == 12 || user_phone.length() == 13)) code = "length_error";
		
		if(code.trim().equals("")) {
			try {
				
				// 첫 번째 '-' 위치
				int pos1 = user_phone.indexOf("-");
				
				// 두 번째 '-' 위치
				int pos2 = user_phone.lastIndexOf("-");
				
				// 첫 '-'는 무조건 위치 3에 있어야 된다.
				if(pos1 != 3) throw new Exception();
				
				// 두 번째 '-'는 무조건 위치 7 ~ 8사이에 있어야 된다.
				if(!(pos2 == 7 || pos2 == 8)) throw new Exception();
				
				// 앞 3자리
				String first = user_phone.substring(0, pos1);
				
				// 중간 3 ~ 4자리
				String second = user_phone.substring(pos1 + 1, pos2);
				
				// 마지막 3자리
				String last = user_phone.substring(pos2 + 1, user_phone.length());
				
				// 핸드폰 번호가 숫자로만 이루어져 있는지 검사
				if(!Pattern.matches(pattern, first + second + last)) throw new Exception();
				
				// 휴대폰 번호가 010이나 011로 시작하는지 검사
				if(!(first.equals("010") || first.substring(0, 3).equals("011"))) throw new Exception();
				
				// 중간 자리가 3 ~ 4자리인지 검사
				if(!(second.length() == 3 || second.length() == 4)) throw new Exception();
				
				// 마지막 자리가 4자리인지 검사
				if(last.length() != 4) throw new Exception();
				
				// 모든 검사를 완료하면 상태 플래그는 permit으로 변경
				code = "permit";
			}catch(Exception e) {
				code = "not_phone";
			}
			
		}
		
		// 상태 플래그가 permit이면 휴대폰이 중복되는지 확인
		if(code.equals("permit")) {
			try {
				String origin_user_phone = userRepository.checkPhone(user_phone);
				code = origin_user_phone == null ? "permit" : "duplication";
			}catch(Exception e) {
				code = "permit";
			}
		}
		
		return code;
		
	}
	
	// 휴대폰이 본인 소유인지 확인을 위해 SMS메시지를 보냄
	@Transactional
	public void sendSMS(String user_phone) {
		
		// 첫 번째 '-' 위치
		int pos1 = user_phone.indexOf("-");
					
		// 두 번째 '-' 위치
		int pos2 = user_phone.lastIndexOf("-");
		
		// 앞 3자리
		String first = user_phone.substring(0, pos1);
					
		// 중간 3 ~ 4자리
		String second = user_phone.substring(pos1 + 1, pos2);
					
		// 마지막 3자리
		String last = user_phone.substring(pos2 + 1, user_phone.length());
		
		String raw_phone = first.trim() + second.trim() + last.trim();
		
		// SMS를 보내기 위한 정보들을 모아 놓은 부분 Start
		
		// API KEY
		String api_key = "NCSXCM7B4PF7EHQB";
		
		// API 비밀KEY
        String api_secret = "KZGN8DOOKPEICT2QSLMRKJE0KW82VMYT";
        
        // 메시지 보내기 객체 생성
		Message sms = new Message(api_key, api_secret);
		
		// SMS를 보내기 위한 정보들을 모아 놓은 부분 End
		
		// 인증 값
		String origin_code = "";
		
		// 랜덤 값 표현을 위한 랜덤 함수
		Random random = new Random();
		
		// 인증 코드에 난수들을 넣는다.(6자리)
		for(int i = 0; i < 6; i++) {
			int ranNum = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(String.valueOf(System.currentTimeMillis()).length() - 1)) + i;
			ranNum = random.nextInt(10) % ranNum;
			origin_code += String.valueOf(ranNum);
		}
		
		// 랜덤 인증 값을 유저의 휴드폰으로 보냄
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("to", raw_phone.trim()); // 수신번호
		map.put("from", "01025392535"); // 발신번호
		map.put("text", "안녕하세요. 인증번호는 [" + origin_code + "] 입니다."); // 문자내용
		map.put("type", "sms"); // 문자 타입
		
		try {
			JSONObject result = sms.send(map);
		} catch (CoolsmsException e) {
			e.printStackTrace();
		}
		
		// 이 인증을 위한 값을 DB에 저장 Start
			
		UserValidation validation = UserValidation.createUserValidation(user_phone, origin_code);
		validation.changeMode();
		userRepository.saveValidation(validation);
		
		// 인증을 위한 값을 DB에 저장 End
			
	}
	
	// 닉네임의 형식을 검사하고 중복을 확인
	public String checkNickname(String user_nickname) {
		
		// 플러터로 반환할 값(상태 플래그)
		String code = "";
		
		// 핸드폰 번호의 형식을 검사할 정규식
		String pattern = "[a-zA-Z0-9가-힣]*";
		
		// 혹시 모를 공백 제거
		user_nickname = user_nickname.trim();
		
		// 닉네임이 2 ~ 10글자 사이인지 검사
		if(!(user_nickname.length() >= 2 && user_nickname.length() <= 10)) code = "length_error";
		
		// 닉네임이 영문 대소문자, 숫자, 한글로만 이루어져 있는지 검사
		if(!Pattern.matches(pattern, user_nickname)) code = "not_nickname";
		
		if(code.trim().equals("")) {
			try {
				// 유저의 닉네임이 이미 존재하는지 확인
				String origin_user_nickname = userRepository.checkNickname(user_nickname);
				
				// 유저의 닉네임이 이미 있다면 error, 없다면 permit으로 세팅
				code = origin_user_nickname == null ? "permit" : "duplication";
			}catch(Exception e) {
				// 에러가 발생 시 permit을 세팅
				code = "permit";
			}
		}
		
		return code;
		
	}
	
	// 인증을 확인
	@Transactional
	public String checkDeviceValidation(String validation_code, String code_type, String user_device) {
		
		// 플러터로 반환할 값(상태 플래그)
		String code = "";
		
		try {
			// 해당하는 인증 메시지가 있는지 검사
			String valid_code = userRepository.checkDeviceValidation(code_type, user_device);
			code = valid_code.equals(validation_code) ? "permit" : "error";
			
			// 해당 유저의 해당 디바이스 타입(email, phone)에 왔던 모든 인증 값들을 지운다.
			if(code.equals("permit")) userRepository.deleteDeviceValidation(code_type, user_device);
		}catch(Exception e) {
			code = "error"; // 반환 값 error로 세팅
		}
		
		return code;
		
	}
	
	// 사용자의 정보를 가져온다.
	public MyPageMainDto getUserInfo(Long user_idx) {
		try {
			return new MyPageMainDto(userRepository.getUserInfo(user_idx));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	// 사용자의 상세 프로필 정보를 가져온다.
	public UserProfileDto getUserDetailInfo(Long user_idx) {
		try {
			return new UserProfileDto(userRepository.getUserInfo(user_idx));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 사용자의 초대 리스트를 가져온다.
	public List<ShowInvitationDto> getInvitationList(Long user_idx) {
		return userRepository.getInvitationList(user_idx).stream().map(i -> new ShowInvitationDto(i)).collect(Collectors.toList());
	}
	
	// ID찾기를 위해 정보를 검증한다.
	@Transactional
	public String checkInfoForFindId(CheckUserInfoForFindIdDto checkInfoDto) {
		
		String user_name = checkInfoDto.getUser_name();
		String user_phone = checkInfoDto.getUser_phone();
		
		String code = "";
		
		try {
			Long user_idx = userRepository.checkInfoForFindId(user_name, user_phone);
			code = "success";
			if(user_idx == null) code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		
		
		if(code.equals("success")) {
			try {
				sendSMS(user_phone); // 사용자의 핸드폰에 SMS 메시지를 보낸다.
			}catch(Exception e) {
				code = "fail";
			}
		}
		
		return code;
		
	}
	
	// ID찾기
	@Transactional
	public String findUserId(FindIdDto findIdDto) {
		
		String user_email = "";
		String user_name = findIdDto.getUser_name();
		String user_phone = findIdDto.getUser_phone();
		
		try {
			user_email = userRepository.findUserId(user_name, user_phone);
		}catch(Exception e) {
			user_email = "";
			e.printStackTrace();
		}
			
		return user_email;
	}
	
	// PW변경을 위해 정보를 비교한다.
	@Transactional
	public String checkInfoForChangePw(CheckUserInfoForChangePwDto checkInfoDto) {
		
		String user_name = checkInfoDto.getUser_name();
		String user_email = checkInfoDto.getUser_email();
		String user_phone = checkInfoDto.getUser_phone();
		
		String code = "";
		
		try {
			Long user_idx = userRepository.checkInfoForChangePw(user_name, user_email, user_phone);
			code = "success";
			if(user_idx == null) code = "fail";
		}catch(Exception e) {
			return "fail";
		}
		
		if(code.equals("success")) {
			try {
				sendSMS(user_phone); // 사용자의 핸드폰에 SMS 메시지를 보낸다.
			}catch(Exception e) {
				code = "fail";
			}
		}
		
		return code;
		
	}
	
	// PW변경
	@Transactional
	public String changeUserPw(ChangePwDto changePwDto) {
		String code = "fail";
		
		String user_pw = changePwDto.getUser_pw();
		String user_name = changePwDto.getUser_name();
		String user_email = changePwDto.getUser_email();
		String user_phone = changePwDto.getUser_phone();
		
		if(changePwDto.getUser_pw().equals(changePwDto.getUser_pw2())) {
			user_pw = passwordEncoder.encode(user_pw);
			int rowNum = userRepository.changeUserPw(user_pw, user_name, user_email, user_phone);
			if(rowNum == 1) code = "success";
		}
		return code;
	}
	
	// 사용자의 프로필 사진을 변경해준다.
	@Transactional
	public ChangeProfilePhotoDto changePhoto(ChangeProfilePhotoDto changeProfilePhotoDto) {
		String code = "";
		
		String absolutePath = "/usr/local/univ_project_image/";
		int ext_pos = changeProfilePhotoDto.getPhoto().getOriginalFilename().lastIndexOf(".");
		String ext = changeProfilePhotoDto.getPhoto().getOriginalFilename().substring(ext_pos, changeProfilePhotoDto.getPhoto().getOriginalFilename().length());
		String file_name = System.currentTimeMillis() + "_" + changeProfilePhotoDto.getPhoto().getOriginalFilename();
		file_name = passwordEncoder.encode(file_name);
		file_name = file_name.replace("\\", "q");
		file_name = file_name.replace("/", "a");
		file_name = file_name.replace(".", "z");
		file_name += ext;
		
		try {
			changeProfilePhotoDto.getPhoto().transferTo(new File(absolutePath + file_name));
			int changedColumn = userRepository.changePhoto(file_name, changeProfilePhotoDto.getUser_idx());
			if(changedColumn == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		
		changeProfilePhotoDto = new ChangeProfilePhotoDto();
	
		if(code.equals("success")) {
			changeProfilePhotoDto.setCode(code);
			changeProfilePhotoDto.setUser_profile_photo(EnvironmentVariableConfig.getPhotoUrl() + file_name);
		}else {
			changeProfilePhotoDto.setCode(code);
			changeProfilePhotoDto.setUser_profile_photo(null);
		}
		
		return changeProfilePhotoDto;
		
	}
	
	// 사용자의 스케쥴 리스트를 가져온다.
	public List<PrivateScheduleListDto> getUserSchedules(Long user_idx) {
		return userRepository.getUserSchedules(user_idx).stream().map(ps -> new PrivateScheduleListDto(ps)).collect(Collectors.toList());
	}
	
	// 사용자의 스케쥴을 추가한다.
	@Transactional
	public String addSchedule(AddPrivateScheduleDto addPrivateScheduleDto) {
		String code = "";
		try {
			int columnNum = userRepository.addSchedule(addPrivateScheduleDto);
			if(columnNum == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		return code;
	}
	
	// 사용자가 초대에 응할지 결정한다.
	@Transactional
	public String decideJoin(DecideJoinProjectDto decideJoinProjectDto) {
		
		String code = "";
		int columnNum = 0;
		try {
			columnNum = userRepository.deleteProjectInvitation(decideJoinProjectDto.getUser_idx(), decideJoinProjectDto.getProject_idx());
			if(columnNum == 1) code = "success";
			else return "fail";
		}catch(Exception e) {
			return "fail";
		}
		if(decideJoinProjectDto.getAccept().equals("Y")) {
		
			columnNum = 0;
			
			try {
				columnNum = userRepository.addProjectMember(decideJoinProjectDto.getProject_idx(), decideJoinProjectDto.getUser_idx());
				if(columnNum == 1) code = "success";
				else return "fail";
			}catch(Exception e) {
				return "fail";
			}
			columnNum = 0;
			
			try {
				columnNum = userRepository.addProjectMemberNum(decideJoinProjectDto.getProject_idx());
				if(columnNum == 1) code = "success";
				else return "fail";
			}catch(Exception e) {
				return "fail";
			}
		}
		return code;
		
	}
	
	// 사용자의 취미를 지운다.
	@Transactional
	public void deleteHobby(Long user_hobby_idxes) {
		userRepository.deleteHobby(user_hobby_idxes);
	}
	
	// 취미 리스트를 가져온다.
	public List<EditHobbyDto> editHobby() {
		return userRepository.getHobbyList().stream().map(hl -> new EditHobbyDto(hl)).collect(Collectors.toList());
	}
	
	// 사용자의 취미를 추가한다.
	@Transactional
	public AddHobbyReturnDto addHobby(AddHobbyDto addHobbyDto) {
		
		// 대분류, 소분류 모두 존재
		if(addHobbyDto.getBig_idx() != -1 && addHobbyDto.getSmall_idx() != -1) {
			userRepository.addHobbyReg(addHobbyDto.getUser_idx(), addHobbyDto.getSmall_idx());
		}
		
		// 대분류만 존재
		if(addHobbyDto.getBig_idx() != -1 && addHobbyDto.getSmall_idx() == -1) {
			List<Long> hobby_idxes = userRepository.searchSmallHobbyTag(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
			if(hobby_idxes.size() == 0) {
				userRepository.addHobbySearchNew(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
			}else {
				userRepository.addHobbySearchAlready(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
			}
			Long search_idx = userRepository.getHobbySearchIdx(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name()).get(0);
			userRepository.addHobbyIl(addHobbyDto.getUser_idx(), search_idx);
		}
		
		// 대분류, 소분류 모두 존재 X
		if(addHobbyDto.getBig_idx() == -1 && addHobbyDto.getSmall_idx() == -1) {
			List<Long> hobby_idxes_big = userRepository.searchBigHobbyTag(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name());
			if(hobby_idxes_big.size() == 0) {
				userRepository.addHobbySearchNew(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
			}else {
				List<Long> hobby_idxes_small = userRepository.searchSmallHobbyTag(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
				if(hobby_idxes_small.size() == 0) {
					userRepository.addHobbySearchNew(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
				}else {
					userRepository.addHobbySearchAlready(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name());
				}
			}
			Long search_idx = userRepository.getHobbySearchIdx(addHobbyDto.getBig_idx(), addHobbyDto.getBig_name(), addHobbyDto.getSmall_name()).get(0);
			userRepository.addHobbyIl(addHobbyDto.getUser_idx(), search_idx);
		}
		return new AddHobbyReturnDto(userRepository.getAddHobbyReturnValue(addHobbyDto.getUser_idx()).get(0));
	}
	
	// 주소를 수정
	@Transactional
	public String editAddress(EditUserAddressDto editUserAddressDto, Long userIdx) {
		if(editUserAddressDto.getMain_addr().trim().equals("")) editUserAddressDto.setMain_addr(null);
		if(editUserAddressDto.getReference_addr().trim().equals("")) editUserAddressDto.setReference_addr(null);
		if(editUserAddressDto.getDetail_addr().trim().equals("")) editUserAddressDto.setDetail_addr(null);
		if(editUserAddressDto.getPost_num().trim().equals("")) editUserAddressDto.setPost_num(null);
		int num = userRepository.editAddress(editUserAddressDto, userIdx);
		return num == 1? "success" : "fail";
	}
	
	// 생년월일, 자격증, MBTI 수정
	@Transactional
	public void editDetailProfile(EditDetailProfile editDetailProfile) {
		
		String flag = editDetailProfile.getFlag();
		String value = editDetailProfile.getValue();
		Long user_idx = editDetailProfile.getUser_idx();
		
		if(flag.equals("birth")) {
			LocalDate user_birth = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
			userRepository.editDetailProfileBirth(user_birth, user_idx);
		}else if(flag.equals("license")) {
			
			Long count = value.chars().filter(c -> c == ',').count();
			String license1 = null;
			String license2 = null;
			String license3 = null;
			
			if(value.length() >= 2) {
				if(count == 0L) {
					license1 = value;
				}else if(count == 1L) {
					license1 = value.substring(0, value.indexOf(','));
					license2 = value.substring(value.indexOf(',') + 1, value.length());
				}else if(count == 2L) {
					license1 = value.substring(0, value.indexOf(','));
					license2 = value.substring(value.indexOf(',') + 1, value.lastIndexOf(','));
					license3 = value.substring(value.lastIndexOf(',') + 1, value.length());
				}
			}
			
			if(license1 != null && license1.trim().equals("")) license1 = null;
			if(license2 != null && license2.trim().equals("")) license2 = null;
			if(license3 != null && license3.trim().equals("")) license3 = null;
			
			userRepository.editDetailProfileLicense(license1, license2, license3, user_idx);
			
		}else if(flag.equals("mbti")) {
			Integer user_mbti = Integer.parseInt(value);
			if(user_mbti >= 18 || user_mbti <= 0) user_mbti = 17;
			userRepository.editDetailProfileMbti(user_mbti, user_idx);
		}
	}
	
	// 이메일 + 전화번호 수정
	@Transactional
	public String editEmailPhone(String type, String value, Long user_idx) {
		String code = "";
		if(type.equals("P")) {
			code = "success";
			
//			String raw_phone = value;
//			int pos1 = raw_phone.indexOf("-");
//			int pos2 = raw_phone.lastIndexOf("-");
//			value = raw_phone.substring(0, pos1) + raw_phone.substring(pos1 + 1, pos2) + raw_phone.substring(pos2 + 1, raw_phone.length());
//			
			userRepository.editDetailProfilePhone(value, user_idx);
		}
		else if(type.equals("E")) {
			code = "success";
			userRepository.editDetailProfileEmail(value, user_idx);
		}else {
			code = "fail";
		}
		return code;
	}
	
	// 닉네임 수정
	@Transactional
	public String editNickname(String user_nickname, Long user_idx) {
		String code = "";
		int executeRow = userRepository.editNickname(user_nickname, user_idx);
		if(executeRow == 1) code = "success";
		else code = "fail";
		return code;
	}
	
	// ===================== 유저 리스트 =====================
	public Map<String, Object> getUserList(Long user_idx, Long project_idx) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Long rowNum = userRepository.checkMemberRight(user_idx, project_idx);
			if(rowNum != 1) {
				resultMap.put("code", "not_leader");
				resultMap.put("result", null);
				return resultMap;
			}
			List<UserListDto> userList = userRepository.getUserList(project_idx).stream().map(u -> new UserListDto(u)).collect(Collectors.toList());
			resultMap.put("code", "success");
			resultMap.put("result", userList);
		}catch(Exception e) {
			resultMap.put("code", "fail");
			resultMap.put("result", null);
		}
		return resultMap;
	}
	// ====================================================
	
	@Transactional
	@Scheduled(cron = "0 30 3 * * *")
	public void inactivateAndDeleteUsers() {
		List<User> users = userRepository.getNotDeletedUsers();
		List<Long> delList = new ArrayList<Long>();
		LocalDate to = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth());
		for (User user : users) {
			LocalDate from = LocalDate.of(user.getLast_logined_datetime().getYear(), user.getLast_logined_datetime().getMonth(), user.getLast_logined_datetime().getDayOfMonth());
			Period period = Period.between(from, to);
			if(period.getDays() > 185) delList.add(user.getUser_idx());
		}
		userRepository.updateDeleteUsers(delList);
	}
	
}
