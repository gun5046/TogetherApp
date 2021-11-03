package univ.together.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.AddProjectScheduleDto;
import univ.together.server.dto.AddProjectTagDto;
import univ.together.server.dto.ManageProjectMemberDto;
import univ.together.server.dto.ModifyProjectInfoDto;
import univ.together.server.dto.ProjectDetailScheduleDto;
import univ.together.server.dto.ProjectDto;
import univ.together.server.dto.ProjectInformationDto;
import univ.together.server.dto.ProjectScheduleDto;
import univ.together.server.dto.TeamApplicationDto;
import univ.together.server.model.Member;
import univ.together.server.model.Project;
import univ.together.server.model.TagList;
import univ.together.server.model.TeamApplication;
import univ.together.server.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	private final List<String>invitationList;
	
	public List<ProjectScheduleDto> getProjectScheduleList(Long project_idx) {
		List<ProjectScheduleDto> projectScheduleDto = null;
		try {
			return projectRepository.getProjectScheduleList(project_idx).stream().map(p -> new ProjectScheduleDto(p)).collect(Collectors.toList());
		}catch(Exception e) {
			projectScheduleDto = new ArrayList<ProjectScheduleDto>();
			return projectScheduleDto;
		}
	}
	
	public ProjectDetailScheduleDto getDetailSchedule(Long schedule_idx) {
		return new ProjectDetailScheduleDto(projectRepository.getDetailSchedule(schedule_idx));
	}
	
	
	public String addInvitation(String user_nickname) {
		if (invitationList.contains(user_nickname))
			return "existed";
		invitationList.add(user_nickname);
		return "success";
	}
	
	@Transactional // 프로젝트 생성
	public void createProject( ProjectDto projectdto) {
		Project project = Project.builder()
				.project_name(projectdto.getProject_name())
				.project_status("A")
				.project_exp(projectdto.getProject_exp())
				.start_date(projectdto.getStart_date())
				.end_date(projectdto.getEnd_date())
				.professionality(projectdto.getProfessionality())
				.project_type(projectdto.getProject_type())
				.open_flag("N")
				.build();
		
		Long pid = projectRepository.CreateProject(project);
		
		
		inviteMember(pid, invitationList);
		
		// 멤버를 INSERT하는 기능(나선재 작성)
		projectRepository.saveMember(projectdto.getUser_idx(), pid);
		for(int i=0; i<projectdto.getTag_num(); i++)
			projectRepository.insertProjectTag(pid, projectdto.getTag_name()[i],projectdto.getDetail_name()[i]);
		
	}
	
	//멤버 초대
	public void inviteMember(Long pid, List<String> invitationList) {
		projectRepository.inviteMember(pid, invitationList);
		invitationList.clear();
	}


	public List<String> searchMember( Long user_idx) {
		return projectRepository.SearchMember(user_idx);
	}
	
	public Object askUserInfo(String user_nickname){
		return projectRepository.askUserInfo(user_nickname);
	}
	
	@Transactional
	public String addSchedule(AddProjectScheduleDto addProjectScheduleDto) {
		String code = "";
		
		try {
			int changedRowNum = projectRepository.addSchedule(addProjectScheduleDto);
			if(changedRowNum == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		
		return code;
	}
	
	@Transactional
	public String inviteUser(Long project_idx, Long user_idx) {
		String code = "fail";
		try {
			Long piRowNum = projectRepository.checkUserProjectInvitation(project_idx, user_idx);
			Long pRowNum = projectRepository.checkUserProject(project_idx, user_idx);
			if(piRowNum == 0 && pRowNum == 0) {
				int rowNum = projectRepository.inviteUser(project_idx, user_idx);
				if(rowNum == 1) code = "success";
				else code = "fail";
			}
		}catch(Exception e) {
			e.printStackTrace();
			code = "fail";
		}
		return code;
	}
	
	@Transactional
	public String removeUser(Long project_idx, Long user_idx, Long logined_user_idx) {
		String code = "";
		try {
			String member_right = projectRepository.getRightUser(project_idx, logined_user_idx);
			if(!member_right.equals("Leader")) throw new Exception();
			int rowNum = projectRepository.removeUser(project_idx, user_idx);
			if(rowNum == 1) rowNum = projectRepository.subProjectMemberNum(project_idx);
			else throw new Exception();
			if(rowNum == 1) code = "success";
			else throw new Exception();
		}catch(Exception e) {
			code = "fail";
		}
		return code;
	}
	
	public List<TagList> getTagList() {
		return projectRepository.getTagList();
	}
	
	// ===================== 나중에 삭제 =====================
	/*
	public ProjectInfoDto getProjectInfo(Long project_idx) {
		ProjectInfoDto projectInfoDto = null;
		try {
			Project projectInfos = projectRepository.getProjectInfo(project_idx);
			projectInfoDto = new ProjectInfoDto(projectInfos);
			projectInfoDto.setCode("success");
			return projectInfoDto;
		}catch(Exception e) {
			projectInfoDto = new ProjectInfoDto();
			projectInfoDto.setCode("error");
			return projectInfoDto;
		}
	}
	
	@Transactional
	public String modifyProjectInfo(ModifyProjectDetailInfoDto modifyProjectDetailInfoDto) {
		String code = "";
		try {
			int rowNum = projectRepository.modifyProjectInfo(modifyProjectDetailInfoDto);
			if(rowNum == 1) code = "success";
			else code = "fail";
		}catch(Exception e) {
			code = "fail";
		}
		return code;
	}
	*/
	
	// ===================== 프로젝트 정보 얻기 =====================
	public ProjectInformationDto getProjectInfo(Long project_idx) {
		try {
			return new ProjectInformationDto(projectRepository.getProjectInfo(project_idx), projectRepository.getProjectTagList(project_idx));
		}catch(Exception e) {
			return null;
		}
	}
	// =========================================================
	
	// ===================== 프로젝트 정보 수정 =====================
	@Transactional
	public String modifyProjectInfo(ModifyProjectInfoDto modifyProjectInfoDto, Long user_idx, Long project_idx) {
		try {
			if(projectRepository.checkUserRight(user_idx, project_idx) == 1) {
				int num = projectRepository.modifyProjectInfo(modifyProjectInfoDto, project_idx);
				if(num == 1) return "success";
				else throw new Exception();
			}else {
				return "not_leader";
			}
		}catch(Exception e) {
			return "fail";
		}
	}
	// =========================================================
	
	// ===================== 팀원 관리 main =====================
	public List<ManageProjectMemberDto> manageMemberMain(Long project_idx) {
		List<ManageProjectMemberDto> mpmd = new ArrayList<ManageProjectMemberDto>();
		List<Member> members = projectRepository.getUserIdxByProjectIdx(project_idx);
		if(members.size() >= 1) {
			for(Member member : members) {
				mpmd.add(new ManageProjectMemberDto(member));
			}
			return mpmd;
		}else return null;
	}
	// ========================================================
	
	// ===================== 팀원 추방 =====================
	@Transactional
	public String removeMember(Long user_idx, Long target_idx, Long project_idx) {
		try {
			if(projectRepository.checkUserRight(user_idx, project_idx) == 1) {
				int num = projectRepository.removeMember(target_idx, project_idx);
				if(num == 1) { num = projectRepository.subProjectMemberNum(project_idx); }
				else { throw new Exception(); }
				if(num ==1) { return "success"; }
				else { throw new Exception(); }
			}else {
				return "not_leader";
			}
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
		
	// ===================== 팀원 수정 =====================
	@Transactional
	public String modifyMember(Long user_idx, Long target_idx, Long project_idx, String member_right) {
		try {
			if(projectRepository.checkUserRight(user_idx, project_idx) == 1) {
				return projectRepository.modifyMember(target_idx, project_idx, member_right) == 1? "success" : "fail";
			}else {
				return "not_leader";
			}
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
	
	// ===================== 태그 삭제 =====================
	@Transactional
	public String deleteProjectTag(Long project_idx, Long user_idx, Long project_tag_idx) {
		try {
			if(projectRepository.checkUserRight(user_idx, project_idx) != 1) return "not_leader";
			if(projectRepository.deleteProjectTag(project_tag_idx) != 1) throw new Exception();
			return "success";
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
	
	// ===================== 태그 추가 =====================
	@Transactional
	public String addProjectTag(AddProjectTagDto addProjectTagDto, Long project_idx, Long user_idx) {
		if(projectRepository.checkUserRight(user_idx, project_idx) != 1) return "not_leader";
		try {
			if(addProjectTagDto.getTag_idx() == 0) {
				// 비정규 데이터(tag_search)
				if(projectRepository.addSearchTag(addProjectTagDto.getTag_name(), addProjectTagDto.getTag_detail_name()) != 1) throw new Exception();
				Long tag_search_idx = projectRepository.getSearchTagIdx(addProjectTagDto.getTag_name(), addProjectTagDto.getTag_detail_name()).get(0);
				if(projectRepository.addProjectTag(project_idx, 0L, tag_search_idx) != 1) throw new Exception();
			}else {
				// 정규 데이터
				if(projectRepository.addProjectTag(project_idx, addProjectTagDto.getTag_idx(), 0L) != 1) throw new Exception();
			}
			return "success";
		}catch(Exception e) {
			return "fail";
		}
	}
	// ===================================================
	
	public List<TeamApplicationDto>getApplicationList(Long project_idx) {
		return projectRepository.getApplicationList(project_idx).stream().map(a-> new TeamApplicationDto(a)).collect(Collectors.toList());
	}
	
	@Transactional
	public void processApplication(Long team_application_idx, Long user_idx, Long project_idx, char flag) {
		if(flag == 'Y') {
			projectRepository.allowApplication(team_application_idx, project_idx, user_idx);
		}else {
			projectRepository.rejectApplication(team_application_idx);
		}
	}
}
