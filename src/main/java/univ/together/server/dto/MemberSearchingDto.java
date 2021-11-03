package univ.together.server.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.SearchCondition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSearchingDto {

	private Integer min_age;
	private Integer max_age;
	private List<String> license = new ArrayList<String>();
	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private List<String> hobby_small_idx = new ArrayList<String>();
	private Boolean save;
	
	public MemberSearchingDto(SearchCondition sc) {
		this.min_age = sc.getMin_age();
		this.max_age = sc.getMax_age();
		if(sc.getLicense1() != null && !sc.getLicense1().equals("")) this.license.add(sc.getLicense1());
		if(sc.getLicense2() != null && !sc.getLicense2().equals("")) this.license.add(sc.getLicense2());
		if(sc.getLicense3() != null && !sc.getLicense3().equals("")) this.license.add(sc.getLicense3());
		if(sc.getMain_addr() != null && !sc.getMain_addr().equals("")) this.main_addr = sc.getMain_addr();
		if(sc.getReference_addr() != null && !sc.getReference_addr().equals("")) this.reference_addr = sc.getReference_addr();
		if(sc.getHobby_small_idx1() != null && !sc.getHobby_small_idx1().equals("")) this.hobby_small_idx.add(sc.getHobby_small_idx1());
		if(sc.getHobby_small_idx2() != null && !sc.getHobby_small_idx2().equals("")) this.hobby_small_idx.add(sc.getHobby_small_idx2());
		if(sc.getHobby_small_idx3() != null && !sc.getHobby_small_idx3().equals("")) this.hobby_small_idx.add(sc.getHobby_small_idx3());
	}
	
}
