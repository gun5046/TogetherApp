package univ.together.server.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddHobbyDto;
import univ.together.server.dto.AddHobbyReturnDto;
import univ.together.server.dto.AddPrivateScheduleDto;
import univ.together.server.dto.AddProjectScheduleDto;
import univ.together.server.dto.ChangeProfilePhotoDto;
import univ.together.server.dto.ChangePwDto;
import univ.together.server.dto.CheckUserInfoForChangePwDto;
import univ.together.server.dto.CheckUserInfoForFindIdDto;
import univ.together.server.dto.DecideJoinProjectDto;
import univ.together.server.dto.EditDetailProfile;
import univ.together.server.dto.EditEmailPhoneDto;
import univ.together.server.dto.EditHobbyDto;
import univ.together.server.dto.EditNicknameDto;
import univ.together.server.dto.EditUserAddressDto;
import univ.together.server.dto.FindIdDto;
import univ.together.server.dto.JoinUserDto;
import univ.together.server.dto.LoginUserDto;
import univ.together.server.dto.MyPageMainDto;
import univ.together.server.dto.PrivateScheduleListDto;
import univ.together.server.dto.ShowInvitationDto;
import univ.together.server.dto.UserProfileDto;
import univ.together.server.dto.ValidationEditEmailDto;
import univ.together.server.dto.ValidationEditPhoneDto;
import univ.together.server.service.UserService;
import univ.together.server.validator.JoinValidator;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "/login")
	public LoginUserDto login(@RequestBody LoginUserDto loginUserDto) {
		userService.login(loginUserDto.getUser_email(), loginUserDto.getUser_pw(), loginUserDto);
		return loginUserDto;
	}
	
	@PostMapping(value = "/join")
	public String join(@Valid @RequestBody JoinUserDto joinUserDto, BindingResult result, Errors errors) {
		new JoinValidator().validate(joinUserDto, errors);
		if(result.hasErrors()) {
			System.out.println(1);
			return null;
		}
		userService.join(joinUserDto);
		System.out.println(2);
		return "a";
	}
	
	@GetMapping(value = "/validationEmail")
	public String validationEmail(@RequestParam(name = "user_email", defaultValue = "") String user_email) {
		String code = userService.checkEmail(user_email.trim());
		if(code.equals("permit")) userService.sendMail(user_email.trim());
		return code;
	}
	
	@GetMapping(value = "/validationPhone")
	public String validationPhone(@RequestParam(name = "user_phone", defaultValue = "") String user_phone) {
		String code = userService.checkPhone(user_phone.trim());
		if(code.equals("permit")) userService.sendSMS(user_phone.trim());
		return code;
	}
	
	@GetMapping(value = "/validationNickname")
	public String validationNickname(@RequestParam(name = "user_nickname", defaultValue = "") String user_nickname) {
		String code = userService.checkNickname(user_nickname.trim());
		return code;
	}
	
	@GetMapping(value = "/checkDeviceValidation")
	public String checkDeviceValidation(@RequestParam(name = "validation_code") String validation_code, 
									   @RequestParam(name = "code_type") String code_type, 
									   @RequestParam(name = "user_device") String user_device) {
		
		String code = userService.checkDeviceValidation(validation_code, code_type, user_device);
		return code;

	}
	
	@GetMapping(value = "/mypage")
	public MyPageMainDto mypage(@RequestParam(name = "user_idx") Long user_idx) {
		return userService.getUserInfo(user_idx);
	}
	
	@GetMapping(value = "/detail_profile")
	public UserProfileDto getDetailProfile(@RequestParam(name = "user_idx") Long user_idx) {
		return userService.getUserDetailInfo(user_idx);
	}
	
	@GetMapping(value = "/delete_hobby")
	public void deleteHobby(@RequestParam(name = "user_hobby_idxes") Long user_hobby_idxes) {
		userService.deleteHobby(user_hobby_idxes);
	}
	
	@GetMapping(value = "/edit_hobby")
	public List<EditHobbyDto> editHobby() {
		return userService.editHobby();
	}
	
	@PostMapping(value = "/add_hobby")
	public AddHobbyReturnDto addHobby(@RequestBody AddHobbyDto addHobbyDto) {
		return userService.addHobby(addHobbyDto);
	}
	
	@PostMapping(value = "/edit_detail_profile")
	public void editDetailProfile(@RequestBody EditDetailProfile editDetailProfile) {
		userService.editDetailProfile(editDetailProfile);
	}
	
	// ================ 주소 수정 ================
	@PostMapping(value = "/edit_address/{userIdx}")
	public String editAddress(@PathVariable(name = "userIdx") Long userIdx, @RequestBody EditUserAddressDto editUserAddressDto) {
		return userService.editAddress(editUserAddressDto, userIdx);
	}
	
	// ================ 이메일 + 전화번호 수정 ================
	
	// 이메일 중복 검사 + 이메일 인증 코드 전송
	@PostMapping(value = "/validationEditEmail")
	public String validationEditEmail(@RequestBody ValidationEditEmailDto validationEditEmailDto) {
		String code = "";
		code = userService.checkEmail(validationEditEmailDto.getUser_email().trim());
		if(code.equals("permit")) userService.sendMail(validationEditEmailDto.getUser_email().trim());
		return code;
	}
	
	// 전화번호 중복 검사 + 전화번호 인증 코드 전송
	@PostMapping(value = "/validationEditPhone")
	public String validationEditPhone(@RequestBody ValidationEditPhoneDto validationEditphoneDto) {
		String code = "";
		code = userService.checkPhone(validationEditphoneDto.getUser_phone().trim());
		if(code.equals("permit")) userService.sendSMS(validationEditphoneDto.getUser_phone().trim());
		return code;
	}
		
	// 이메일 or 전화번호 수정
	@PostMapping(value = "/editEmailPhone")
	public String editEmailPhone(@RequestBody EditEmailPhoneDto editEmailPhoneDto) {
		String code = "";
		if(editEmailPhoneDto.getCode().trim().equals("true")) {
			code = userService.editEmailPhone(editEmailPhoneDto.getType(), editEmailPhoneDto.getValue(), editEmailPhoneDto.getUser_idx());
		}
		return code;
	}
		
	// ===================================================
	
	// 닉네임 변경
	@PostMapping(value = "/editNickname")
	public String editNickname(@RequestBody EditNicknameDto editNicknameDto) {
		String code = "fail";
		code = userService.editNickname(editNicknameDto.getUser_nickname(), editNicknameDto.getUser_idx());
		return code;
	}
	
	@GetMapping(value = "/invitationList")
	public List<ShowInvitationDto> getInvitationList(@RequestParam(name = "user_idx") Long user_idx) {
		return userService.getInvitationList(user_idx);
	}
	
	// =================== ID찾기, PW변경 ===================
	
	// ID찾기를 위한 검증 + SMS메시지 보내기
	@PostMapping(value = "/checkInfoForFindId")
	public String checkInfoForFindId(@RequestBody CheckUserInfoForFindIdDto checkInfoDto) {
		return userService.checkInfoForFindId(checkInfoDto);
	}
	
	// ID찾기
	@PostMapping(value = "/findUserId")
	public String findUserId(@RequestBody FindIdDto findIdDto) {
		return userService.findUserId(findIdDto);
	}
	
	// PW변경을 위한 검증 + SMS메시지 보내기
	@PostMapping(value = "/checkInfoForChangePw")
	public String checkInfoForChangePw(@RequestBody CheckUserInfoForChangePwDto checkInfoDto) {
		return userService.checkInfoForChangePw(checkInfoDto);
	}
	
	// PW변경
	@PostMapping(value = "/changePw")
	public String changePw(@Valid @RequestBody ChangePwDto changePwDto, BindingResult result) {
		if(result.hasErrors()) {
			return "fail";
		}
		return userService.changeUserPw(changePwDto);
	}
	
	// ====================================================
	
	@PostMapping(value = "/changePhoto")
	public ChangeProfilePhotoDto changePhoto(@ModelAttribute(name = "changeProfilePhotoDto") ChangeProfilePhotoDto changeProfilePhotoDto) {
		System.out.println(changeProfilePhotoDto.getPhoto().getOriginalFilename());
		System.out.println(changeProfilePhotoDto.getUser_idx());
		return userService.changePhoto(changeProfilePhotoDto);
	}
	
	@PostMapping(value = "/addSchedule")
	public String addSchedule(@RequestBody AddPrivateScheduleDto addPrivateScheduleDto) {
		return userService.addSchedule(addPrivateScheduleDto);
	}
	
	@GetMapping(value = "/getUserSchedules")
	public List<PrivateScheduleListDto> getUserSchedules(Long user_idx) {
		return userService.getUserSchedules(user_idx);
	}
	
	@PostMapping(value = "/decideJoin")
	public String decideJoin(@RequestBody DecideJoinProjectDto decideJoinProjectDto) {
		return userService.decideJoin(decideJoinProjectDto);
	}
	
	// ===================== 유저 리스트 =====================
	@PostMapping(value = "/getUsers/{userIdx}/{projectIdx}")
	public Map<String, Object> getUserList(@PathVariable(name = "userIdx") Long userIdx, @PathVariable(name = "projectIdx") Long projectIdx) {
		return userService.getUserList(userIdx, projectIdx);
	}
	// ====================================================
	
}