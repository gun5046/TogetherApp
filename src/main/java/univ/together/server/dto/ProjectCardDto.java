package univ.together.server.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectCard;
import univ.together.server.model.ProjectTag;

@Data
public class ProjectCardDto {
	
	private Long project_idx;
	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private int member_num;
	private String comment;
	private int my_flag=1;
	
	private List<String> tag= new ArrayList<>();
	private List<String> tag_detail = new ArrayList<>();
	public ProjectCardDto(ProjectCard pc) {
		this.project_idx = pc.getProject_idx().getProject_idx();
		this.project_name=pc.getProject_idx().getProject_name();
		this.project_exp=pc.getProject_idx().getProject_exp();
		this.start_date=pc.getProject_idx().getStart_date();
		this.end_date=pc.getProject_idx().getEnd_date();
		this.professionality=pc.getProject_idx().getProfessionality();
		this.project_type=pc.getProject_idx().getProject_type();
		this.member_num=pc.getProject_idx().getMember_num();
		this.comment=pc.getComment();
	}
	
	public ProjectCardDto(Project p, String comment){
		this.project_idx = p.getProject_idx();
		this.project_name=p.getProject_name();
		this.project_exp=p.getProject_exp();
		this.start_date=p.getStart_date();
		this.end_date=p.getEnd_date();
		this.professionality=p.getProfessionality();
		this.project_type=p.getProject_type();
		this.member_num=p.getMember_num();
		this.comment=comment;
		
		for (ProjectTag t : p.getTags()) {
			if(t.getTag_search_idx().getTag_search_idx()==0) {
				tag.add(t.getTag_idx().getTag_name());
				tag_detail.add(t.getTag_idx().getTag_detail_name());
			}
			else {
				tag.add(t.getTag_search_idx().getSearch_name());
				tag_detail.add(t.getTag_search_idx().getSearch_detail_name());
			}
		}
		
	}

	public ProjectCardDto(Project p){
		this.project_idx = p.getProject_idx();
		this.project_name=p.getProject_name();
		this.project_exp=p.getProject_exp();
		this.start_date=p.getStart_date();
		this.end_date=p.getEnd_date();
		this.professionality=p.getProfessionality();
		this.project_type=p.getProject_type();
		this.member_num=p.getMember_num();
		this.comment="";
		
		for (ProjectTag t : p.getTags()) {
			if(t.getTag_search_idx().getTag_search_idx()==0) {
				tag.add(t.getTag_idx().getTag_name());
				tag_detail.add(t.getTag_idx().getTag_detail_name());
			}
			else {
				tag.add(t.getTag_search_idx().getSearch_name());
				tag_detail.add(t.getTag_search_idx().getSearch_detail_name());
			}
		}
		
	}
	
}
