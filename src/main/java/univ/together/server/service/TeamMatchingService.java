package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.CreateCardDto;
import univ.together.server.dto.Pair;
import univ.together.server.dto.ProjectCardDto;
import univ.together.server.dto.SearchingTableDto;
import univ.together.server.model.Project;
import univ.together.server.model.TagList;
import univ.together.server.repository.ProjectRepository;
import univ.together.server.repository.TeamMatchingRepository;
import univ.together.server.repository.UserRepository;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMatchingService {
	
	private final TeamMatchingRepository teammatchingRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;

	
	
	
	
	public List<ProjectCardDto> CreateProjectCardMain(Long user_idx){
		return teammatchingRepository.findSearchNotAvailableProject(user_idx).stream().map(p->new ProjectCardDto(p)).collect(Collectors.toList());
	}
	@Transactional
	public List<ProjectCardDto> getProjectCardList(Long user_idx) {
		return teammatchingRepository.getProjectCardList(user_idx);
	}
	
	@Transactional
	public void completeCreateCard(CreateCardDto ccd) {
		teammatchingRepository.completeCreateCard(ccd);
	}
	
	@Transactional
	public void deleteCard(Long project_idx) {
		teammatchingRepository.deleteCard(project_idx);
	}
	
	@Transactional
	public List<ProjectCardDto> teamMatchingMain(Long user_idx) {
		List<Long>project_idx_list = new ArrayList<>();
		List<ProjectCardDto> card_list = new ArrayList<>();
		try {
			project_idx_list.addAll(teammatchingRepository.findSearchAvailableProject(user_idx));
			
		
			for (Long idx : project_idx_list) {
				try {
					card_list.add(teammatchingRepository.getTeamMatchingInfo(idx));
				}catch(Exception e) {
					System.out.println("null");
				}
			}
		}catch (Exception e) {
			return new ArrayList<ProjectCardDto>();
		}
		
		card_list.addAll(teammatchingRepository.teamSearching(user_idx));
		
		return card_list;
	}
	@Transactional
	public List<TagList> searchingTable() {
		return projectRepository.getTagList();
	}
	
	@Transactional
	public void saveSearchingTable(SearchingTableDto searchingtabledto) {
		teammatchingRepository.saveSearchingTable(searchingtabledto);
	}
	
	public List<ProjectCardDto> searchingMain(SearchingTableDto dto){
		return teammatchingRepository.searchingMain(dto);
	}
	@Transactional
	public String submitApplication(Long user_idx, Long project_idx) {
		return teammatchingRepository.submitApplication(user_idx,project_idx);
	}
	
	
}

