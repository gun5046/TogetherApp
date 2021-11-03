package univ.together.server.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name="TEAM_SEARCH_CONDITION")
@Setter(value = AccessLevel.PRIVATE)
public class TeamSearchCondition {
	@Id
	@Column(name = "TEAM_SEARCH_CONDITION_IDX")
	private Long team_search_condition_idx;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDX")
	private User user_idx;
	
	@Column(name = "START_DATE")
	private LocalDate start_date;
	
	@Column(name = "END_DATE")
	private LocalDate end_date;
	@Column(name = "PROFESSIONALITY")
	private String professionality;
	
	@Column(name = "PROJECT_TYPE")
	private String project_type;
	
	@Column(name = "TAG_NAME")
	private String tag_name;
	
	@Column(name = "TAG_DETAIL_NAME")
	private String tag_detail_name;
	
	@Column(name="MEMBER_NUM")
	private int member_num;
}
