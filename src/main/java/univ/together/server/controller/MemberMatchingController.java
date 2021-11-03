package univ.together.server.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MemberProfileInfoDto;
import univ.together.server.dto.MemberSearchingDto;
import univ.together.server.dto.RegisterSearchMemberProfileCardDto;
import univ.together.server.dto.SearchInviteMemberDto;
import univ.together.server.dto.SearchMemberProfileCardDto;
import univ.together.server.service.MemberMatchingService;

@RestController
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberMatchingController {

	private final MemberMatchingService memberMatchingService;
	
	// 팀원 검색 main page
	// 검색조건이 저장되었다면, 추천 리스트를 return
	// 검색조건이 저장되지 않았다면, 추천 리스트는 null
	@GetMapping(value = "/search/main/{userIdx}")
	public List<SearchMemberProfileCardDto> mainPage(@PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getUserProfileCard(userIdx);
	}
	
	// 카드 등록하는 페이지로 이동
	@GetMapping(value = "/search/register/{userIdx}")
	public MemberProfileInfoDto registerCardPage(@PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getUserProfileInfo(userIdx);
	}
	
	// 카드 등록
	@PostMapping(value = "/search/register/{userIdx}")
	public void registerCard(@PathVariable(name = "userIdx") Long userIdx, 
											 @RequestBody RegisterSearchMemberProfileCardDto registerSearchMemberProfileCardDto) {
		memberMatchingService.registerUpdateProfileCard(userIdx, registerSearchMemberProfileCardDto);
	}
	
	// 사용자 card list
	@GetMapping(value = "/search/cards/{userIdx}")
	public List<SearchMemberProfileCardDto> cardList(@PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getProfileCardList(userIdx);
	}
	
	// 팀원 검색 완료
	@PostMapping(value = "/search/do/{userIdx}")
	public List<SearchMemberProfileCardDto> memberSearch(@RequestBody MemberSearchingDto memberSearchingDto, 
							 @PathVariable(name = "userIdx") Long userIdx) {
		return memberMatchingService.getSearchResult(userIdx, memberSearchingDto);
	}
	
	// 팀원 초대
	@PostMapping(value = "/search/invite")
	public String invitation(@RequestBody SearchInviteMemberDto searchInviteMemberDto) {
		return memberMatchingService.invitation(searchInviteMemberDto);
	}
	
}
