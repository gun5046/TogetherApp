package univ.together.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;


import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.dto.RegisterSearchMemberProfileCardDto;
import univ.together.server.model.SearchCondition;
import univ.together.server.model.SearchMember;
import univ.together.server.model.User;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepository {

	private final EntityManager em;
	
	// user의 프로필 카드를 가져옴
	public SearchMember getUserProfileCard(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm JOIN FETCH sm.user_idx " + 
							  "WHERE sm.user_idx.user_idx = :user_idx AND sm.user_idx.delete_flag = :delete_flag", SearchMember.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// user의 저장된 검색 조건을 가져옴
	public SearchCondition getUserSearchCondition(Long user_idx) {
		return em.createQuery("SELECT sc FROM SearchCondition sc WHERE sc.user_idx.user_idx = :user_idx", SearchCondition.class)
				.setParameter("user_idx", user_idx)
				.getSingleResult();
	}
	
	// user의 프로필 카드가 없는 경우, user의 정보를 가져온다.
	public User getUserProfileInfo(Long user_idx) {
		return em.createQuery("SELECT u FROM User u JOIN FETCH u.user_mbti " + 
							  "WHERE u.user_idx = :user_idx AND u.delete_flag = :delete_flag", User.class)
				.setParameter("user_idx", user_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// user의 프로필 카드를 업데이트
	public void updateUserProfileCard(Long user_idx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		em.createQuery("UPDATE SearchMember sm SET sm.resume = :resume, sm.comment = :comment " + 
					   "WHERE sm.user_idx.user_idx = :user_idx")
				.setParameter("resume", registerSearchMemberProfileCardDto.getResume())
				.setParameter("comment", registerSearchMemberProfileCardDto.getComment())
				.setParameter("user_idx", user_idx)
				.executeUpdate();
	}
	
	// user의 프로필 카드를 등록
	public void registerUserProfileCard(Long user_idx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		em.createNativeQuery("INSERT INTO search_member (user_idx, resume, comment) " + 
							 "VALUES(:user_idx, :resume, :comment)")
				.setParameter("user_idx", user_idx)
				.setParameter("resume", registerSearchMemberProfileCardDto.getResume())
				.setParameter("comment", registerSearchMemberProfileCardDto.getComment())
				.executeUpdate();
	}
	
	// user들의 프로필 카드 리스트(최근 등록자 top5)
	public List<SearchMember> getProfileCardList(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm " + 
							  "WHERE sm.user_idx.delete_flag = :delete_flag AND " + 
							  "sm.user_idx.user_idx != :user_idx " + 
							  "ORDER BY sm.search_member_idx DESC", SearchMember.class)
				.setParameter("delete_flag", "N")
				.setParameter("user_idx", user_idx)
				.setFirstResult(0)
				.setMaxResults(5)
				.getResultList();
	}
	
	// user들의 프로필 카드 리스트(모든 등록자)
	public List<SearchMember> getAllProfileCardList(Long user_idx) {
		return em.createQuery("SELECT sm FROM SearchMember sm " + 
							  "WHERE sm.user_idx.delete_flag = :delete_flag AND " + 
							  "sm.user_idx.user_idx != :user_idx", SearchMember.class)
				.setParameter("delete_flag", "N")
				.setParameter("user_idx", user_idx)
				.getResultList();
	}
	
	// user의 기존 검색조건 존재 여부 확인
	public void deleteSearchCondition(Long user_idx) {
		em.createQuery("DELETE FROM SearchCondition WHERE user_idx.user_idx = :user_idx")
				.setParameter("user_idx", user_idx)
				.executeUpdate();
	}
	
	// user의 검색조건 저장
	public int saveSearchCondition(Long user_idx, MemberSearchingDto memberSearchingDto) {
		return em.createNativeQuery("INSERT INTO search_condition " + 
									"(min_age, max_age, license1, license2, license3, main_addr, reference_addr, user_idx, hobby_small_idx1, hobby_small_idx2, hobby_small_idx3) VALUES " + 
									"(:min_age, :max_age, :license1, :license2, :license3, :main_addr, :reference_addr, :user_idx, :hobby_small_idx1, :hobby_small_idx2, :hobby_small_idx3)")
				.setParameter("min_age", memberSearchingDto.getMin_age())
				.setParameter("max_age", memberSearchingDto.getMax_age())
				.setParameter("license1", memberSearchingDto.getLicense().get(0))
				.setParameter("license2", memberSearchingDto.getLicense().get(1))
				.setParameter("license3", memberSearchingDto.getLicense().get(2))
				.setParameter("main_addr", memberSearchingDto.getMain_addr())
				.setParameter("reference_addr", memberSearchingDto.getReference_addr())
				.setParameter("user_idx", user_idx)
				.setParameter("hobby_small_idx1", memberSearchingDto.getHobby_small_idx().get(0))
				.setParameter("hobby_small_idx2", memberSearchingDto.getHobby_small_idx().get(1))
				.setParameter("hobby_small_idx3", memberSearchingDto.getHobby_small_idx().get(2))
				.executeUpdate();
	}
	
	// 초대를 하는 유저가 리더인지 확인
	public Long checkUserLeader(Long user_idx, Long project_idx) {
		return em.createQuery("SELECT COUNT(m) FROM Member m WHERE " + 
							  "m.user_idx.user_idx = :user_idx AND " + 
							  "m.project_idx.project_idx = :project_idx AND " + 
							  "m.member_right = :member_right AND " + 
							  "m.user_idx.delete_flag = :delete_flag", Long.class)
				.setParameter("user_idx", user_idx)
				.setParameter("project_idx", project_idx)
				.setParameter("member_right", "Leader")
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// 이미 팀원인지 확인
	public Long checkUserMember(Long member_idx, Long project_idx) {
		return em.createQuery("SELECT COUNT(m) FROM Member m WHERE " + 
							  "m.user_idx.user_idx = :user_idx AND " + 
							  "m.project_idx.project_idx = :project_idx AND " + 
							  "m.user_idx.delete_flag = :delete_flag", Long.class)
				.setParameter("user_idx", member_idx)
				.setParameter("project_idx", project_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// 이미 초대 메시지를 보냈는지 확인
	public Long checkSentUser(Long member_idx, Long project_idx) {
		return em.createQuery("SELECT COUNT(pi) FROM ProjectInvitation pi WHERE " + 
							  "pi.project_idx.project_idx = :project_idx AND " + 
							  "pi.user_idx.user_idx = :user_idx AND " + 
							  "pi.user_idx.delete_flag = :delete_flag", Long.class)
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", member_idx)
				.setParameter("delete_flag", "N")
				.getSingleResult();
	}
	
	// 팀원 초대
	public int inviteMember(Long project_idx, Long member_idx) {
		return em.createNativeQuery("INSERT INTO project_invitation (project_idx, user_idx, invite_datetime) " + 
									"VALUES (:project_idx, :user_idx, :invite_datetime)")
				.setParameter("project_idx", project_idx)
				.setParameter("user_idx", member_idx)
				.setParameter("invite_datetime", LocalDateTime.now())
				.executeUpdate();
	}
	
}
