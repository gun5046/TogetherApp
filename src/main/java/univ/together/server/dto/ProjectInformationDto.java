package univ.together.server.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.Member;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectTag;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInformationDto {

	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private List<String> tag_names = new ArrayList<String>();
	private List<String> tag_detail_names = new ArrayList<String>();
	private List<Long> tag_list_idxes = new ArrayList<Long>();
	private List<String> member_names = new ArrayList<String>();
	
	public ProjectInformationDto(Project project, List<ProjectTag> projectTag) {
		this.project_name = project.getProject_name();
		this.project_exp = project.getProject_exp();
		this.start_date = project.getStart_date();
		this.end_date = project.getEnd_date();
		this.professionality = project.getProfessionality();
		this.project_type = project.getProject_type();
		
		for(ProjectTag tags : projectTag) {
			if(tags.getTag_idx().getTag_idx() == 0) {
				this.tag_names.add(tags.getTag_search_idx().getSearch_name());
				this.tag_detail_names.add(tags.getTag_search_idx().getSearch_detail_name());
			}else {
				this.tag_names.add(tags.getTag_idx().getTag_name());
				this.tag_detail_names.add(tags.getTag_idx().getTag_detail_name());
			}
			this.tag_list_idxes.add(tags.getProject_tag_idx());
		}
		
		for(Member member : project.getMembers()) member_names.add(member.getUser_idx().getUser_name());
	}
	
}
