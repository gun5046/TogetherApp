package univ.together.server.repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddPrivateScheduleDto;
import univ.together.server.dto.EditUserAddressDto;
import univ.together.server.model.PrivateSchedule;
import univ.together.server.model.ProjectInvitation;
import univ.together.server.model.User;
import univ.together.server.model.UserHobbyCatSmall;
import univ.together.server.model.UserHobbyList;
import univ.together.server.model.UserValidation;

@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final EntityManager em;
	
	public void changeLastLoginedDatetime(Long user_idx) {
		em.createQuery("UPDATE User u SET u.last_logined_datetime = :last_logined_datetime WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("last_logined_datetime", LocalDateTime.now())
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public Long checkInfoForFindId(String user_name, String user_phone) {
		return em.createQuery("SELECT u.user_idx FROM User u WHERE " + 
							"u.user_name = :user_name AND u.user_phone = :user_phone AND u.delete_flag = :delete_flag", Long.class)
				.setParameter("user_name", user_name)
				.setParameter("user_phone", user_phone)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public Long checkInfoForChangePw(String user_name, String user_email, String user_phone) {
		return em.createQuery("SELECT u.user_idx FROM User u WHERE " + 
							"u.user_name = :user_name AND " + 
							"u.user_phone = :user_phone AND " + 
							"u.user_email = :user_email AND " + 
							"u.delete_flag = :delete_flag", Long.class)
				.setParameter("user_name", user_name)
				.setParameter("user_phone", user_phone)
				.setParameter("user_email", user_email)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getPwByEmail(String user_email) {
		return em.createQuery("SELECT u.user_pw FROM User u WHERE u.user_email = :user_email AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_email", user_email)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public Long getUserIdxByEmail(String user_email) {
		return em.createQuery("SELECT u.user_idx FROM User u WHERE u.user_email = :user_email AND u.delete_flag = :delete_flag", Long.class)
				.setParameter("user_email", user_email)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserNaeByIdx(Long user_idx) {
		return em.createQuery("SELECT u.user_name FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserProfilePhoto(Long user_idx) {
		return em.createQuery("SELECT u.user_profile_photo FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public String getUserNameByIdx(Long user_idx) {
		return em.createQuery("SELECT u.user_name FROM User u WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", String.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public void joinUser(User user) {
		em.persist(user);
	}
	
	public void initUserMbti(String user_name, String user_nickname) {
		em.createQuery("UPDATE User u SET u.user_mbti.mbti_idx = :user_mbti " + 
						"WHERE u.user_name = :user_name AND u.user_nickname = :user_nickname AND u.delete_flag = :delete_flag")
				.setParameter("user_mbti", 17)
				.setParameter("user_name", user_name)
				.setParameter("user_nickname", user_nickname)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public String checkEmail(String user_email) {
		return em.createQuery("SELECT u.user_email FROM User u WHERE u.user_email = :user_email", String.class)
				.setParameter("user_email", user_email)
				.getSingleResult();
	}
	
	public String checkPhone(String user_phone) {
		return em.createQuery("SELECT u.user_phone FROM User u WHERE u.user_phone = :user_phone", String.class)
				.setParameter("user_phone", user_phone)
				.getSingleResult();
	}
	
	public String checkNickname(String user_nickname) {
		return em.createQuery("SELECT u.user_nickname FROM User u WHERE u.user_nickname = :user_nickname", String.class)
				.setParameter("user_nickname", user_nickname)
				.getSingleResult();
	}
	
	public void saveValidation(UserValidation validation) {
		em.persist(validation);
	}
	
	public String checkDeviceValidation(String code_type, String user_device) {
		return em.createQuery("SELECT uv.validation_value FROM UserValidation uv " + 
							"WHERE uv.validation_flag = :validation_flag " + 
							"AND uv.user_device = :user_device " + 
							"ORDER BY uv.validation_idx DESC", String.class)
				.setParameter("validation_flag", code_type)
				.setParameter("user_device", user_device)
				.setFirstResult(0)
				.setMaxResults(1)
				.getSingleResult();
	}
	
	public void deleteHobby(Long user_hobby_idxes) {
		em.createQuery("DELETE FROM UserHobbyList uhl " + 
							"WHERE uhl.user_hobby_idx = :user_hobby_idx")
				.setParameter("user_hobby_idx", user_hobby_idxes)
				.executeUpdate();
	}
	
	public List<UserHobbyCatSmall> getHobbyList() {
		return em.createQuery("SELECT uhcs FROM UserHobbyCatSmall uhcs JOIN FETCH uhcs.user_hobby_cat_big_idx " + 
							"ORDER BY uhcs.user_hobby_cat_big_idx.user_hobby_cat_big_idx, uhcs.user_hobby_cat_small_idx", UserHobbyCatSmall.class)
				.getResultList();
	}
	
	// ========================= 취미 추가 =========================
	
	// 대분류, 소분류 모두 존재
	public void addHobbyReg(Long user_idx, Long hobby_idx) {
		em.createNativeQuery("INSERT INTO user_hobby_list (user_idx, hobby_idx, search_flag) " + 
							"VALUES (:user_idx, :hobby_idx, :search_flag)")
				.setParameter("user_idx", user_idx)
				.setParameter("hobby_idx", hobby_idx)
				.setParameter("search_flag", "N")
				.executeUpdate();
	}

	// 대분류나 소분류 존재 X
	public void addHobbyIl(Long user_idx, Long hobby_idx) {
		em.createNativeQuery("INSERT INTO user_hobby_list (user_idx, hobby_search_idx, search_flag) " + 
							"VALUES (:user_idx, :hobby_idx, :search_flag)")
				.setParameter("user_idx", user_idx)
				.setParameter("hobby_idx", hobby_idx)
				.setParameter("search_flag", "Y")
				.executeUpdate();
	}
	
	public List<Long> getHobbySearchIdx(Long user_hobby_cat_big_idx, String user_hobby_cat_big_name, String user_hobby_cat_small_name) {
		return em.createQuery("SELECT uhs.user_hobby_search_idx FROM UserHobbySearch uhs WHERE " + 
							"uhs.user_hobby_cat_big_idx = :user_hobby_cat_big_idx AND " + 
							"uhs.user_hobby_cat_big_name = :user_hobby_cat_big_name AND " + 
							"uhs.user_hobby_cat_small_name = :user_hobby_cat_small_name", Long.class)
				.setParameter("user_hobby_cat_big_idx", user_hobby_cat_big_idx)
				.setParameter("user_hobby_cat_big_name", user_hobby_cat_big_name)
				.setParameter("user_hobby_cat_small_name", user_hobby_cat_small_name)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();
	}
	
	// 대분류를 hobby_search테이블에서 찾는다.
	public List<Long> searchBigHobbyTag(Long user_hobby_cat_big_idx, String user_hobby_cat_big_name) {
		return em.createQuery("SELECT uhs.user_hobby_cat_big_idx FROM UserHobbySearch uhs WHERE " + 
						"uhs.user_hobby_cat_big_idx = :user_hobby_cat_big_idx AND " + 
						"uhs.user_hobby_cat_big_name = :user_hobby_cat_big_name", Long.class)
				.setParameter("user_hobby_cat_big_idx", user_hobby_cat_big_idx)
				.setParameter("user_hobby_cat_big_name", user_hobby_cat_big_name)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();
	}
	
	// 소분류를 hobby_search테이블에서 찾는다.
	public List<Long> searchSmallHobbyTag(Long user_hobby_cat_big_idx, String user_hobby_cat_big_name, String user_hobby_cat_small_name) {
		return em.createQuery("SELECT uhs.user_hobby_search_idx FROM UserHobbySearch uhs WHERE " + 
						"uhs.user_hobby_cat_big_idx = :user_hobby_cat_big_idx AND " + 
						"uhs.user_hobby_cat_big_name = :user_hobby_cat_big_name AND " + 
						"uhs.user_hobby_cat_small_name = :user_hobby_cat_small_name", Long.class)
				.setParameter("user_hobby_cat_big_idx", user_hobby_cat_big_idx)
				.setParameter("user_hobby_cat_big_name", user_hobby_cat_big_name)
				.setParameter("user_hobby_cat_small_name", user_hobby_cat_small_name)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();
	}
	
	// hobby_search 테이블에 추가
	public void addHobbySearchNew(Long user_hobby_cat_big_idx, String user_hobby_cat_big_name, String user_hobby_cat_small_name) {
		em.createNativeQuery("INSERT INTO user_hobby_search (count, user_hobby_cat_big_idx, user_hobby_cat_big_name, user_hobby_cat_small_name) " + 
						"VALUES (:count, :user_hobby_cat_big_idx, :user_hobby_cat_big_name, :user_hobby_cat_small_name)")
				.setParameter("count", 1)
				.setParameter("user_hobby_cat_big_idx", user_hobby_cat_big_idx)
				.setParameter("user_hobby_cat_big_name", user_hobby_cat_big_name)
				.setParameter("user_hobby_cat_small_name", user_hobby_cat_small_name)
				.executeUpdate();
	}
	
	// hobby_search 테이블에서 count 추가
	public void addHobbySearchAlready(Long user_hobby_cat_big_idx, String user_hobby_cat_big_name, String user_hobby_cat_small_name) {
		em.createQuery("UPDATE UserHobbySearch uhs SET uhs.count = uhs.count + :count WHERE " + 
						"uhs.user_hobby_cat_big_idx = :user_hobby_cat_big_idx AND " + 
						"uhs.user_hobby_cat_big_name = :user_hobby_cat_big_name AND " + 
						"uhs.user_hobby_cat_small_name = :user_hobby_cat_small_name")
				.setParameter("count", 1)
				.setParameter("user_hobby_cat_big_idx", user_hobby_cat_big_idx)
				.setParameter("user_hobby_cat_big_name", user_hobby_cat_big_name)
				.setParameter("user_hobby_cat_small_name", user_hobby_cat_small_name)
				.executeUpdate();
	}
	
	// return value
	public List<UserHobbyList> getAddHobbyReturnValue(Long user_idx) {
		return em.createQuery("SELECT uhl FROM UserHobbyList uhl LEFT OUTER JOIN FETCH uhl.hobby_idx LEFT OUTER JOIN FETCH uhl.hobby_search_idx WHERE " + 
						"uhl.user_idx.user_idx = :user_idx ORDER BY uhl.user_hobby_idx DESC", UserHobbyList.class)
				.setParameter("user_idx", user_idx)
				.setFirstResult(0)
				.setMaxResults(1)
				.getResultList();
	}
	
	// ===========================================================
	
	// ======================== 주소 수정 =========================
	public Integer editAddress(EditUserAddressDto editUserAddressDto, Long userIdx) {
		return em.createQuery("UPDATE User u SET u.address.main_addr = :main_addr, " + 
							  "u.address.reference_addr = :reference_addr, " + 
							  "u.address.detail_addr = :detail_addr, " + 
							  "u.address.post_num = :post_num WHERE " + 
							  "u.user_idx = :user_idx AND " + 
							  "u.delete_flag = :delete_flag")
				.setParameter("main_addr", editUserAddressDto.getMain_addr())
				.setParameter("reference_addr", editUserAddressDto.getReference_addr())
				.setParameter("detail_addr", editUserAddressDto.getDetail_addr())
				.setParameter("post_num", editUserAddressDto.getPost_num())
				.setParameter("user_idx", userIdx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// ======================== 프로필 수정 =========================
	
	// 생년월일 수정
	public void editDetailProfileBirth(LocalDate user_birth, Long user_idx) {
		em.createQuery("UPDATE User u SET u.user_birth = :user_birth WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("user_birth", user_birth)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// 자격증 수정
	public void editDetailProfileLicense(String license1, String license2, String license3, Long user_idx) {
		em.createQuery("UPDATE User u SET u.license1 = :license1, u.license2 = :license2, u.license3 = :license3 WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("license1", license1)
				.setParameter("license2", license2)
				.setParameter("license3", license3)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// MBTI 수정
	public void editDetailProfileMbti(Integer mbti_idx, Long user_idx) {
		em.createQuery("UPDATE User u SET u.user_mbti.mbti_idx = :mbti_idx WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("mbti_idx", mbti_idx)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// 전화번호 수정
	public void editDetailProfilePhone(String phone, Long user_idx) {
		em.createQuery("UPDATE User u SET u.user_phone = :phone WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("phone", phone)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// 이메일 수정
	public void editDetailProfileEmail(String email, Long user_idx) {
		em.createQuery("UPDATE User u SET u.user_email = :email WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("email", email)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// 닉네임 수정
	public int editNickname(String user_nickname, Long user_idx) {
		return em.createQuery("UPDATE User u SET u.user_nickname = :user_nickname WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("user_nickname", user_nickname)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	// ===========================================================
	
	public void deleteDeviceValidation(String code_type, String user_device) {
		em.createQuery("DELETE FROM UserValidation uv " + 
				"WHERE uv.validation_flag = :validation_flag AND uv.user_device = :user_device")
				.setParameter("validation_flag", code_type)
				.setParameter("user_device", user_device)
				.executeUpdate();
	}
	
	public User getUserInfo(Long user_idx) {
		return em.createQuery("SELECT DISTINCT u FROM User u LEFT OUTER JOIN FETCH u.user_mbti WHERE u.user_idx = :user_idx", User.class)
				.setParameter("user_idx", user_idx)
				.getSingleResult();
	}
	
	public List<ProjectInvitation> getInvitationList(Long user_idx) {
		return em.createQuery("SELECT pi FROM ProjectInvitation pi " + 
							"JOIN FETCH pi.project_idx " + 
							"WHERE pi.user_idx.user_idx = :user_idx " + 
							"ORDER BY pi.project_invitation_idx DESC", ProjectInvitation.class)
				.setParameter("user_idx", user_idx)
				.getResultList();
	}
	
	public String findUserId(String user_name, String user_phone) {
		return em.createQuery("SELECT u.user_email FROM User u WHERE " + 
							"u.user_name = :user_name AND " + 
							"u.user_phone = :user_phone AND " + 
							"u.delete_flag = :delete_flag", String.class)
				.setParameter("user_name", user_name)
				.setParameter("user_phone", user_phone)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	public int changeUserPw(String user_pw, String user_name, String user_email, String user_phone) {
		return em.createQuery("UPDATE User u SET u.user_pw = :user_pw " + 
							"WHERE u.user_name = :user_name AND " + 
							"u.user_email = :user_email AND " + 
							"u.user_phone = :user_phone AND " + 
							"u.delete_flag = :delete_flag")
				.setParameter("user_name", user_name)
				.setParameter("user_email", user_email)
				.setParameter("user_phone", user_phone)
				.setParameter("user_pw", user_pw)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public int changePhoto(String user_profile_photo, Long user_idx) {
		return em.createQuery("UPDATE User u SET u.user_profile_photo = :user_profile_photo WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag")
				.setParameter("user_profile_photo", user_profile_photo)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.executeUpdate();
	}
	
	public int addSchedule(AddPrivateScheduleDto addPrivateScheduleDto) {
		return em.createNativeQuery("INSERT INTO private_schedule (title, user_idx, body, start_datetime, end_datetime) " + 
							"VALUES (:title, :user_idx, :body, :start_datetime, :end_datetime)")
				.setParameter("title", addPrivateScheduleDto.getSchedule_name())
				.setParameter("user_idx", addPrivateScheduleDto.getWriter_idx())
				.setParameter("body", addPrivateScheduleDto.getSchedule_content())
				.setParameter("start_datetime", addPrivateScheduleDto.getSchedule_start_datetime())
				.setParameter("end_datetime", addPrivateScheduleDto.getSchedule_end_datetime())
				.executeUpdate();
	}
	
	public List<PrivateSchedule> getUserSchedules(Long user_idx) {
		return em.createQuery("SELECT ps FROM PrivateSchedule ps " + 
							"WHERE ps.user_idx.user_idx = :user_idx " + 
							"AND ps.user_idx.delete_flag = :delete_flag " + 
							"ORDER BY ps.private_schedule_idx DESC", PrivateSchedule.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getResultList();
	}
	
	// ====================================== 초대 수락/거부 기능 START ======================================
	
	public int deleteProjectInvitation(Long user_idx, Long project_idx) {
		return em.createQuery("DELETE FROM ProjectInvitation pi WHERE pi.user_idx.user_idx = :user_idx AND pi.project_idx.project_idx = :project_idx")
				.setParameter("user_idx", user_idx)
				.setParameter("project_idx", project_idx)
				.executeUpdate();
	}
	
	public int addProjectMember(Long project_idx, Long user_idx) {
		return em.createNativeQuery("INSERT INTO member (project_idx, user_idx, member_right) VALUES (:project_idx, :user_idx, :member_right)")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", user_idx)
				.setParameter("member_right", "Member")
				.executeUpdate();
	}
	
	public int addProjectMemberNum(Long project_idx) {
		return em.createQuery("UPDATE Project p SET p.member_num = p.member_num + :plus_num WHERE p.project_idx = :project_idx")
				.setParameter("plus_num", 1)
				.setParameter("project_idx", project_idx)
				.executeUpdate();
	}
	
	// ====================================== 초대 수락/거부 기능 END ======================================
	
	// ===================== 권한 확인 =====================
	public Long checkMemberRight(Long user_idx, Long project_idx) {
		return em.createQuery("SELECT COUNT(m) FROM Member m WHERE " + 
							  "m.user_idx.user_idx = :user_idx AND " + 
							  "m.project_idx.project_idx = :project_idx AND " + 
							  "m.member_right = :member_right", Long.class)
				.setParameter("user_idx", user_idx)
				.setParameter("project_idx", project_idx)
				.setParameter("member_right", "Leader")
				.getSingleResult();
	}
	// ===================================================
	
	// ===================== 유저 리스트 =====================
	public List<User> getUserList(Long project_idx) {
		return em.createQuery("SELECT DISTINCT m.user_idx FROM Member m " + 
							  "WHERE m.project_idx.project_idx != :project_idx", User.class)
				.setParameter("project_idx", project_idx)
				.getResultList();
	}
	// ====================================================

	
	// ====================================== 서비스 최근 이용 날짜가 190일이 넘은 유저를 비활성화 한다. ======================================
	public List<User> getNotDeletedUsers() {
		return em.createQuery("SELECT u FROM User u WHERE u.delete_flag = :delete_flag", User.class)
				.setParameter("delete_flag", "N")
				.getResultList();
	}
	
	public void updateDeleteUsers(List<Long> users) {
		em.createQuery("UPDATE User u SET (u.delete_flag, u.deleted_datetime) = (:delete_flag, :deleted_datetime) WHERE u.user_idx IN (:user_idx)")
				.setParameter("delete_flag", "Y")
				.setParameter("deleted_datetime", LocalDateTime.now())
				.setParameter("user_idx", users)
				.executeUpdate();
	}
	// ==========================================================================================================================
	
}
