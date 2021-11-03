package univ.together.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.CreateCardDto;
import univ.together.server.dto.ProjectCardDto;
import univ.together.server.dto.SearchingTableDto;
import univ.together.server.model.Project;
import univ.together.server.model.TagList;
import univ.together.server.service.TeamMatchingService;

@RestController
@RequestMapping(value="/teamMatching")
@RequiredArgsConstructor
public class TeamMatchingController { // 매칭에 필요한 정보 : 프로젝트 전문성, 프로젝트 유형, 시작기간,종료기간, mbti정보(팀장만 확인? 팀원 모두 확인? 명확하지않음)
	private final TeamMatchingService matchingService;
	
	@GetMapping()
	public List<ProjectCardDto> teamMatchingMain(Long user_idx) {
		return matchingService.teamMatchingMain(user_idx);
	}
	
	//CreateCard
	@GetMapping(value="/projectList")
	public List<ProjectCardDto> CreateProjectCardMain(Long user_idx) {
		return matchingService.CreateProjectCardMain(user_idx);
	}
	//GetAllCardList
	@GetMapping(value="/projectList/card")
	public List<ProjectCardDto> getProjectCardList(Long user_idx) {
		return matchingService.getProjectCardList(user_idx);
	}
	
	//CompleteCreateCard
	@PostMapping(value="/projectList/card/build") //project_idx & comment
	public String completeCreateCard(@RequestBody CreateCardDto ccd) {
		try {
		matchingService.completeCreateCard(ccd);
		}catch(Exception e) {
			System.out.println(e);
			return "failed";
		}
		return "success";
	}
	
	@GetMapping(value="/card/disable")
	public String deleteCard(Long project_idx) {
		try {
			matchingService.deleteCard(project_idx);
		}
		catch(Exception e) {
			return "failed";
		}
		return "success";
	}
	
	
	@GetMapping(value="/team/condition")
	public List<TagList> searchingTable() {
		return matchingService.searchingTable();
	}
	
	@PostMapping(value="/team/condition/table")
	public List<ProjectCardDto> saveSearchingTable(@RequestBody SearchingTableDto searchingtabledto) {
		try {
		matchingService.saveSearchingTable(searchingtabledto);
		}catch(Exception e) {
			return new ArrayList<>();
		}
		return matchingService.searchingMain(searchingtabledto);
	}
	
	@GetMapping(value="/team/application")
	public String submitApplication(@RequestParam("user_idx")Long user_idx, @RequestParam("project_idx") Long project_idx) {
		return matchingService.submitApplication(user_idx,project_idx);
	}
}
