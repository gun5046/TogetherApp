package univ.together.server.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class Address {
	
	@Column(name = "MAIN_ADDR")
	private String main_addr;
	
	@Column(name = "REFERENCE_ADDR")
	private String reference_addr;
	
	@Column(name = "DETAIL_ADDR")
	private String detail_addr;
	
	@Column(name = "POST_NUM")
	private String post_num;
	
}
