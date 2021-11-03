package univ.together.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Abc {

	@GetMapping(value = "/hello")
	public String abc() {
		return "NewFile";
	}
	
}
