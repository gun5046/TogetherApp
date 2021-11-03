package univ.together.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair{
	private Long x;
	private Long y;
	private String s1;
	private String s2;
	
	public Pair(Long x, int y){
		this.x= x;
		this.y= (long) y;
	}
	public Pair(int x, Long y){
		this.x=(long)x;
		this.y=y;
	}
	
	public Pair(String s1, String s2) {
		this.s1=s1;
		this.s2=s2;
	}
}
