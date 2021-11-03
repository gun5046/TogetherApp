package univ.together.server.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Project {

	@Id
	@Column(name = "PROJECT_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long project_idx;
	
	@Column(name = "PROJECT_NAME")
	private String project_name;
	
	@Column(name = "PROJECT_STATUS")
	private String project_status; //Enum A I
	
	@Column(name = "PROJECT_EXP")
	private String project_exp; //세부내용
	
	@Column(name = "START_DATE")
	private LocalDate start_date;
	
	@Column(name = "END_DATE")
	private LocalDate end_date;
	
	@Column(name = "PROFESSIONALITY")	//High , Mid, Low, Any
	private String professionality;
	
	@Column(name = "PROJECT_TYPE") //Study, Out, In, Any
	private String project_type;
	
	@Column(name = "OPEN_FLAG")
	private String open_flag;
	
	@Column(name = "MEMBER_NUM")
	private int member_num;
	
	@OneToMany(mappedBy = "project_idx")
	@JsonIgnore
	private List<Member> members = new ArrayList<Member>();
	
	@OneToMany(mappedBy = "project_idx")
	@JsonIgnore
	private List<ProjectTag> tags = new ArrayList<ProjectTag>();
	
	@OneToMany(mappedBy = "project_idx")
	@JsonIgnore
	private List<ProjectSchedule> projectSchedules = new ArrayList<ProjectSchedule>();
	
	@OneToMany(mappedBy = "project_idx")
	@JsonIgnore
	private List<File> files = new ArrayList<File>();
	
	@Builder
	public Project(String project_name, String project_status, String project_exp, LocalDate start_date, 
			LocalDate end_date, String professionality, String project_type, String open_flag) {
		this.project_name=project_name;
		this.project_status=project_status;
		this.project_exp=project_exp;
		this.start_date=start_date;
		this.end_date=end_date;
		this.professionality=professionality;
		this.project_type=project_type;
		this.open_flag=open_flag;
		this.member_num=1;
	}
	
}
