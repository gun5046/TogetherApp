package univ.together.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberProfileInfoDto;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.dto.RegisterSearchMemberProfileCardDto;
import univ.together.server.dto.SearchInviteMemberDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.model.SearchMember;
import univ.together.server.repository.MemberMatchingRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberMatchingService {
	
	private final MemberMatchingRepository memberMatchingRepository;
	
	// user의 프로필 카드를 가져옴
	public List<SearchMemberProfileCardDto> getUserProfileCard(Long userIdx) {
		SearchMemberProfileCardDto searchMemberProfileCardDtoreturn;
		List<SearchMemberProfileCardDto> savedList;
		List<SearchMemberProfileCardDto> returnList = new ArrayList<SearchMemberProfileCardDto>();
		try {
			searchMemberProfileCardDtoreturn = new SearchMemberProfileCardDto(memberMatchingRepository.getUserProfileCard(userIdx));
			returnList.add(searchMemberProfileCardDtoreturn);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			savedList = calResult(memberMatchingRepository.getAllProfileCardList(userIdx), new MemberSearchingDto(memberMatchingRepository.getUserSearchCondition(userIdx)));
			for(SearchMemberProfileCardDto searchMemberProfileCardDto : savedList) {
				returnList.add(searchMemberProfileCardDto);
			}
			return returnList;
		}catch(Exception e) {
			e.printStackTrace();
			return returnList;
		}
	}
	
	// null --> 내 프로필 X, 추천 X
	// [a, b] --> 내 user_idx랑 같은 것은 내 profile_card, 다른건 추천 list profile_card
	
	// user의 프로필 카드가 없는 경우, user의 정보를 가져온다.
	public MemberProfileInfoDto getUserProfileInfo(Long userIdx) {
		return new MemberProfileInfoDto(memberMatchingRepository.getUserProfileInfo(userIdx));
	}
	
	// 카드를 등록/업데이트 한다.
	@Transactional
	public void registerUpdateProfileCard(Long userIdx, RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		try {
			memberMatchingRepository.getUserProfileCard(userIdx);
			memberMatchingRepository.updateUserProfileCard(userIdx, registerSearchMemberProfileCardDto);
		}catch(Exception e) {
			memberMatchingRepository.registerUserProfileCard(userIdx, registerSearchMemberProfileCardDto);
		}
	}
	
	// user들의 프로필 카드 리스트(최근 등록자 top5)
	public List<SearchMemberProfileCardDto> getProfileCardList(Long userIdx) {
		return memberMatchingRepository.getProfileCardList(userIdx).stream().map(sm -> new SearchMemberProfileCardDto(sm)).collect(Collectors.toList());
	}
	
	// 검색 결과(가장 맞는 user top3)
	@Transactional
	public List<SearchMemberProfileCardDto> getSearchResult(Long userIdx, MemberSearchingDto memberSearchingDto) {
		if(memberSearchingDto.getSave() == true) {
			memberMatchingRepository.deleteSearchCondition(userIdx);
			for(int i = memberSearchingDto.getLicense().size(); i < 3; i++) memberSearchingDto.getLicense().add(null);
			for(int i = memberSearchingDto.getHobby_small_idx().size(); i < 3; i++) memberSearchingDto.getHobby_small_idx().add(null);
			memberMatchingRepository.saveSearchCondition(userIdx, memberSearchingDto);
		}
		memberSearchingDto.getLicense().removeAll(Collections.singletonList(null));
		memberSearchingDto.getHobby_small_idx().removeAll(Collections.singletonList(null));
		memberSearchingDto.getLicense().removeAll(Collections.singletonList(""));
		memberSearchingDto.getHobby_small_idx().removeAll(Collections.singletonList(""));
		return calResult(memberMatchingRepository.getAllProfileCardList(userIdx), memberSearchingDto);
	}
	
	// 검색 결과 연산
	public List<SearchMemberProfileCardDto> calResult(List<SearchMember> cardList, MemberSearchingDto memberSearchingDto) {
		int num = 0;
		List<SearchMemberProfileCardDto> resultList = new ArrayList<SearchMemberProfileCardDto>();
		List<SearchMemberProfileCardDto> list = new ArrayList<SearchMemberProfileCardDto>();
		for (SearchMember searchMember : cardList) {
			if(searchMember.getUser_idx().getUser_age() >= memberSearchingDto.getMin_age() && 
					searchMember.getUser_idx().getUser_age() <= memberSearchingDto.getMax_age()) {
				num += 1;
			}
			if(memberSearchingDto.getLicense().size() >= 1) {
				for(String license : memberSearchingDto.getLicense()) {
					if(license == null || license.equals("")) continue;
					if(license.equals(searchMember.getUser_idx().getLicense1()) || 
						license.equals(searchMember.getUser_idx().getLicense2()) || 
						license.equals(searchMember.getUser_idx().getLicense3())) {
						num += 1;
					}
				}
			}
			
			if(searchMember.getUser_idx().getAddress() != null && 
					!searchMember.getUser_idx().getAddress().getMain_addr().equals("") && 
					!searchMember.getUser_idx().getAddress().getReference_addr().equals("") && 
					searchMember.getUser_idx().getAddress().getMain_addr() != null && 
					searchMember.getUser_idx().getAddress().getReference_addr() != null && 
					memberSearchingDto.getMain_addr() != null && memberSearchingDto.getReference_addr() != null &&
					!memberSearchingDto.getMain_addr().equals("") && !memberSearchingDto.getReference_addr().equals("")) {
				if(searchMember.getUser_idx().getAddress().getMain_addr().equals(memberSearchingDto.getMain_addr())) {
					num += 1;
					if(searchMember.getUser_idx().getAddress().getReference_addr().equals(memberSearchingDto.getReference_addr())) {
						num += 1;
					}
				}
			}
			
			if(memberSearchingDto.getHobby_small_idx().size() >= 1) {
				int hobby_filter_num = memberSearchingDto.getHobby_small_idx().size();
				int hobby_num = searchMember.getUser_idx().getUser_hobbies().size();
				for(int i = 0; i < hobby_num; i++) {
					for(int j = 0; j < hobby_filter_num; j++) {
						if(searchMember.getUser_idx().getUser_hobbies() != null && 
								searchMember.getUser_idx().getUser_hobbies().get(i).getHobby_idx() != null && 
								searchMember.getUser_idx().getUser_hobbies().get(i).getHobby_idx().getUser_hobby_cat_small_idx() == 
								Long.parseLong(memberSearchingDto.getHobby_small_idx().get(j))) {
							num += 1;
						}
					}
				}
			}
			
			SearchMemberProfileCardDto smpcd = new SearchMemberProfileCardDto(searchMember);
			smpcd.setNum(num);
			resultList.add(smpcd);
				
			num = 0;
		}
		
		resultList = resultList.stream().sorted(Comparator.comparing(SearchMemberProfileCardDto::getNum).reversed()).collect(Collectors.toList());
		
		if(resultList.size() >= 3) {
			for(int i = 0; i < 3; i++) list.add(resultList.get(i));
		}else {
			for(int i = 0; i < resultList.size(); i++) list.add(resultList.get(i));
		}
		
		return list;
	}
	
	// 팀원 초대
	@Transactional
	public String invitation(SearchInviteMemberDto searchInviteMemberDto) {
		try {
			if(searchInviteMemberDto.getMember_idx() == searchInviteMemberDto.getUser_idx()) return "self_invite";
			boolean isLeader = checkUserLeader(searchInviteMemberDto.getUser_idx(), searchInviteMemberDto.getProject_idx());
			if(!isLeader) return "not_leader";
			boolean isMember = checkUserMember(searchInviteMemberDto.getMember_idx(), searchInviteMemberDto.getProject_idx());
			if(!isMember) return "already_in";
			boolean isSent = checkSentUser(searchInviteMemberDto.getMember_idx(), searchInviteMemberDto.getProject_idx());
			if(!isSent) return "already_sent";
			boolean isSuccess = inviteMember(searchInviteMemberDto.getMember_idx(), searchInviteMemberDto.getProject_idx());
			if(!isSuccess) throw new Exception();
			return "success";
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	// 초대를 하는 유저가 리더인지 확인
	public boolean checkUserLeader(Long user_idx, Long project_idx) {
		return memberMatchingRepository.checkUserLeader(user_idx, project_idx) == 1? true : false;
	}
	
	// 이미 팀원인지 확인
	public boolean checkUserMember(Long member_idx, Long project_idx) {
		return memberMatchingRepository.checkUserMember(member_idx, project_idx) == 0? true : false;
	}
	
	// 이미 초대 메시지를 보냈는지 확인
	public boolean checkSentUser(Long member_idx, Long project_idx) {
		return memberMatchingRepository.checkSentUser(member_idx, project_idx) == 0? true : false;
	}
	
	// 팀원 초대
	public boolean inviteMember(Long member_idx, Long project_idx) {
		return memberMatchingRepository.inviteMember(project_idx, member_idx) == 1? true : false;
	}
	
}
