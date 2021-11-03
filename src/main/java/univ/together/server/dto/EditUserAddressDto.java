package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserAddressDto {

	private String main_addr;
	private String reference_addr;
	private String detail_addr;
	private String post_num;
	
}
