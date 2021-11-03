package univ.together.server.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.CreateCardDto;
import univ.together.server.dto.Pair;
import univ.together.server.dto.ProjectCardDto;
import univ.together.server.dto.SearchingTableDto;
import univ.together.server.model.Project;
import univ.together.server.model.ProjectCard;
import univ.together.server.model.TeamSearchCondition;

@Repository
@RequiredArgsConstructor
public class TeamMatchingRepository {

	private final EntityManager em;

	// user 정보 가져옴
//	public User getUserInfo(Long user_idx) {
//		return em.createQuery("SELECT u FROM user u WHERE u.user_idx = : user_idx",User.class)
//				.setParameter("user_idx", user_idx)
//				.getSingleResult();
//	}
//	
	
	//팀카드를 가지고 있는 서치가능한 팀리스트
	public List<Long> findSearchAvailableProject(Long user_idx) {
		return em.createQuery("SELECT p.project_idx FROM Project p JOIN Member m ON m.project_idx.project_idx = p.project_idx WHERE p.open_flag = 'Y' AND m.user_idx.user_idx = :user_idx AND p.project_status='A'",Long.class)
				.setParameter("user_idx", user_idx).getResultList();
	}
	
	//projectCardList 리턴
		public ProjectCardDto getTeamMatchingInfo(Long project_idx) {
			
			System.out.println("repo");
			Project p = em.createQuery("SELECT p FROM Project p WHERE p.project_idx = :project_idx AND project_status='A' ",Project.class).setParameter("project_idx", project_idx).getSingleResult();
			System.out.println("p err");
			String comment = em.createQuery("SELECT pc.comment FROM ProjectCard pc WHERE pc.project_idx.project_idx = :project_idx AND pc.project_idx.project_status='A' ",String.class).setParameter("project_idx", project_idx).getSingleResult();
			System.out.println("c err");
			ProjectCardDto pc = new ProjectCardDto(p,comment);
			System.out.println("dto err");
			return pc;
		}
		
	/*
	 * private Long project_idx;
	private String project_name;
	private String project_exp;
	private LocalDate start_date;
	private LocalDate end_date;
	private String professionality;
	private String project_type;
	private int member_num;
	private String comment;
	 */
		
		
	//projectCard 모두 리턴
	public List<ProjectCardDto> getProjectCardList(Long user_idx) {
		List<ProjectCard> pc = em.createQuery("SELECT pc FROM ProjectCard pc",ProjectCard.class).getResultList();
		
		
		return pc.stream().map(c -> new ProjectCardDto(c)).collect(Collectors.toList());
		
	}
	// 카드 만들기
	public void completeCreateCard(CreateCardDto ccd) {
		em.createQuery("UPDATE Project p SET p.open_flag='Y' WHERE p.project_idx = :project_idx").setParameter("project_idx", ccd.getProject_idx()).executeUpdate();
		
		try {
			em.createQuery("SELECT pc FROM ProjectCard pc WHERE pc.project_idx.project_idx = :project_idx").setParameter("project_idx", ccd.getProject_idx()).getSingleResult();
			em.createQuery("UPDATE ProjectCard pc SET pc.comment = :comment WHERE pc.project_idx.project_idx = :project_idx").setParameter("comment", ccd.getComment())
			.setParameter("project_idx", ccd.getProject_idx()).executeUpdate();
		}
		catch(Exception e) {
			
			em.createNativeQuery("INSERT INTO project_card(project_idx, comment) VALUES(:project_idx, :comment)").
			setParameter("project_idx", ccd.getProject_idx()).setParameter("comment", ccd.getComment()).executeUpdate();		
		}
	
	}
	//카드 삭제
	public void deleteCard(Long project_idx) {
		em.createQuery("DELETE FROM ProjectCard pc WHERE pc.project_idx.project_idx = :project_idx").setParameter("project_idx", project_idx).executeUpdate();
		em.createQuery("UPDATE Project p SET p.open_flag='N' WHERE p.project_idx = :project_idx").setParameter("project_idx", project_idx).executeUpdate();
	}
	
	//card없는팀 (카드 만들때 사용)
	public List<Project> findSearchNotAvailableProject(Long user_idx){
		return em.createQuery("SELECT p FROM Project p JOIN Member m ON m.project_idx = p.project_idx WHERE p.open_flag='N' AND m.user_idx.user_idx = :user_idx AND project_status='A' ",Project.class)
				.setParameter("user_idx", user_idx).getResultList();
	}
	
	//Create projectCard

	
	

	// 팀 매칭
	// 검색할 태그들이 tag_list에 있는지확인, 있으면 idx 가져옴/ 없으면 tag_seach에서 찾고 없으면 -> tag_search에
	// 새로 대입 / 있으면 search_idx 값 가져옴
	public Long getTagIdx(String tag_name, String tag_detail_name) {

		return em
				.createQuery(
						"SELECT t.tag_idx FROM TagList t WHERE t.tag_name =: tag_name AND t.tag_detail_name =:detail_name",
						Long.class)
				.setParameter("detail_name", tag_detail_name).setParameter("tag_name", tag_name).getSingleResult();
	}

	public Long getTagSearchIdx(String tag_name, String tag_detail_name) {

		em.createNativeQuery("INSERT INTO tag_search(search_name, search_detail_name) VALUES(:search_name, :search_detail_name)")
				.setParameter("search_name", tag_name).setParameter("search_detail_name", tag_detail_name)
				.executeUpdate();

		return em.createQuery(
				"SELECT t.tag_search_idx FROM TagSearch t WHERE t.search_name =: tag_name AND t.search_detail_name=:"
						+ "tag_detail_name",
				Long.class).setParameter("tag_name", tag_name).setParameter("tag_detail_name", tag_detail_name)
				.setFirstResult(0).setMaxResults(1).getSingleResult();
	}

	public List<ProjectCardDto> searchingMain(SearchingTableDto dto){
		List<ProjectCardDto> list = new ArrayList<>();
		List<Long> p = new ArrayList<>();
		int m=0;
		int n=0;
		Long tag_idx;
		try {
			tag_idx = getTagIdx(dto.getTag_name(), dto.getTag_detail_name());
		}catch(Exception e) {
			tag_idx = (long)0;
		}
		
		if (tag_idx==0) {
			n=1;
		}
		
		if(n==1) {
			
			try {
				p = em.createQuery("SELECT p.project_idx FROM Project p WHERE p.project_status = :project_status  AND p.member_num <= :member_num "
				 		+ "AND p.professionality Like :professionality AND p.project_type Like :project_type"
							+ " AND p.open_flag =:open_flag AND p.start_date >=:start_date AND p.end_date <= :end_date",Long.class)
					.setParameter("professionality", "%"+dto.getProfessionality()+"%")
					.setParameter("open_flag", "Y")
					.setParameter("project_status", "A")
					.setParameter("project_type", "%"+dto.getProject_type()+"%")
					.setParameter("member_num", dto.getMember_num())
					.setParameter("start_date",dto.getStart_date())
					.setParameter("end_date", dto.getEnd_date())
					.getResultList();
			}catch(Exception e) {
				return new ArrayList<>();
			}
		}else {
			try {
				p= em.createQuery("SELECT p.project_idx FROM Project p JOIN ProjectTag t ON t.project_idx.project_idx = p.project_idx WHERE p.project_status = :project_status  AND p.member_num <= :member_num "
				 		+ "AND p.professionality Like :professionality AND p.project_type Like :project_type"
							+ " AND p.open_flag =:open_flag AND p.start_date >=:start_date AND p.end_date <= :end_date AND t.tag_idx.tag_idx =:tag_idx",Long.class)
					.setParameter("professionality", "%"+dto.getProfessionality()+"%")
					.setParameter("open_flag", "Y")
					.setParameter("project_status", "A")
					.setParameter("project_type", "%"+dto.getProject_type()+"%")
					.setParameter("member_num", dto.getMember_num())
					.setParameter("start_date",dto.getStart_date())
					.setParameter("end_date", dto.getEnd_date())
					.setParameter("tag_idx", tag_idx)
					.getResultList();
			}catch(Exception e) {
				return new ArrayList<>();
			}
		}
		for( Long a : p) {
			if(m==3) {
				break;
			}
			list.add(getTeamMatchingInfo(a));
			m++;
		}
		return list;
	}
	
	public List<ProjectCardDto> teamSearching(Long user_idx) {
		int m=0;
		int n=0;
		Long tag_idx;
		TeamSearchCondition t = null;
		ProjectCardDto pc;
		List<Long> p = new ArrayList<>();
		List <ProjectCardDto> list = new ArrayList<>();
		
		
		try {
			t = em.createQuery("SELECT t FROM TeamSearchCondition t WHERE t.user_idx.user_idx = :user_idx",TeamSearchCondition.class).setParameter("user_idx", user_idx).getSingleResult();
		}
		catch(Exception e) {
			return new ArrayList<>();
		}
		try {
			tag_idx = getTagIdx(t.getTag_name(), t.getTag_detail_name());
		}catch(Exception e) {
			System.out.println("태그없음");
			tag_idx=(long) 0;
		}
		if (tag_idx==0) {
			n=1;
		}
		System.out.println(t.getProfessionality());
		System.out.println(t.getProject_type());
		System.out.println(t.getStart_date());
		System.out.println(t.getEnd_date());
		System.out.println(t.getMember_num());
		System.out.println(n);
		System.out.println("tag_idx="+tag_idx);
		if(n==1) {
			try { // any 는 빈 공백으로 보내주세요
				 p = em.createQuery("SELECT p.project_idx FROM Project p WHERE p.project_status = :project_status  AND p.member_num <= :member_num "
				 		+ "AND p.professionality Like :professionality AND p.project_type Like :project_type"
							+ " AND p.open_flag =:open_flag AND p.start_date >=:start_date AND p.end_date <= :end_date",Long.class)
					.setParameter("professionality", "%"+t.getProfessionality()+"%")
					.setParameter("open_flag", "Y")
					.setParameter("project_status", "A")
					.setParameter("project_type", "%"+t.getProject_type()+"%")
					.setParameter("member_num", t.getMember_num())
					.setParameter("start_date",t.getStart_date())
					.setParameter("end_date", t.getEnd_date())
					.getResultList();
				}catch(Exception e) {
				System.out.println(e);
				return new ArrayList<>();
			}
		}
		else{
			try { // any 는 빈 공백으로 보내주세요
		
				p= em.createQuery("SELECT p.project_idx FROM Project p JOIN ProjectTag t ON t.project_idx.project_idx = p.project_idx WHERE p.project_status = :project_status  AND p.member_num <= :member_num "
				 		+ "AND p.professionality Like :professionality AND p.project_type Like :project_type"
							+ " AND p.open_flag =:open_flag AND p.start_date >=:start_date AND p.end_date <= :end_date AND t.tag_idx.tag_idx =:tag_idx",Long.class)
					.setParameter("professionality", "%"+t.getProfessionality()+"%")
					.setParameter("open_flag", "Y")
					.setParameter("project_status", "A")
					.setParameter("project_type", "%"+t.getProject_type()+"%")
					.setParameter("member_num", t.getMember_num())
					.setParameter("start_date",t.getStart_date())
					.setParameter("end_date", t.getEnd_date())
					.setParameter("tag_idx", tag_idx)
					.getResultList();
			}catch(Exception e) {
				System.out.println(e);
				return new ArrayList<>();
			}
		}
		System.out.println("사이즈"+p.size());
		for(Long a : p) {
			if(m==3) {
				break;
			}
			System.out.println(a);
			try {
			pc = getTeamMatchingInfo(a);
			pc.setMy_flag(0);
			list.add(pc);
			System.out.println(pc.getStart_date());
			m++;
			}catch(Exception e) {
				continue;
			}
		}
		return list;
	
				
		// 값이 없으면 다른값으로 추가
	}
	
	
	//서치 테이블 저장
	public void saveSearchingTable(SearchingTableDto searchingtabledto ) {
		em.createQuery("DELETE FROM TeamSearchCondition t WHERE t.user_idx.user_idx= :user_idx").setParameter("user_idx", searchingtabledto.getUser_idx()).executeUpdate();
		em.createNativeQuery("INSERT INTO team_search_condition(user_idx, start_date, end_date, professionality, project_type, tag_name, tag_detail_name, member_num) VALUES(:user_idx"
				+ ", :start_date, :end_date, :professionality, :project_type, :tag_name, :tag_detail_name, :member_num)").setParameter("user_idx", searchingtabledto.getUser_idx())
		.setParameter("start_date", searchingtabledto.getStart_date()).setParameter("end_date", searchingtabledto.getEnd_date())
		.setParameter("professionality", searchingtabledto.getProfessionality())
		.setParameter("project_type", searchingtabledto.getProject_type()).setParameter("tag_name", searchingtabledto.getTag_name())
		.setParameter("tag_detail_name", searchingtabledto.getTag_detail_name()).executeUpdate();// date 상관없을때 디폴트값 넣어주기
	}
	
	// 지원서 제출
	public String submitApplication(Long user_idx, Long project_idx) {
		Long n = em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.project_idx.project_idx = :project_idx AND m.user_idx.user_idx = :user_idx",Long.class).setParameter("project_idx", project_idx)
		.setParameter("user_idx", user_idx).getSingleResult();
		System.out.println("asd");
		Long m = em.createQuery("SELECT COUNT(t) FROM TeamApplication t WHERE t.user_idx.user_idx = :user_idx AND t.project_idx.project_idx = :project_idx",Long.class).setParameter("user_idx", user_idx)
				.setParameter("project_idx", project_idx).getSingleResult();
		System.out.println("qwe");
		if(n >= 1 || m >= 1) {
			return "failed";
		}
		
		em.createNativeQuery("INSERT INTO team_application(user_idx, project_idx) VALUES(:user_idx, :project_idx)").setParameter("user_idx", user_idx).setParameter("project_idx", project_idx).executeUpdate();
		return "success";
	}
}
