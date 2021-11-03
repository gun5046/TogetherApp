package univ.together.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.ProjectListDto;
import univ.together.server.service.MainService;

@RestController
@RequiredArgsConstructor
public class MainController {

	private final MainService mainService;
	
	@GetMapping(value = "/main")
	public List<ProjectListDto> main(@RequestParam(name = "user_idx") Long user_idx) throws Exception {
		return mainService.findProjectByUserIdx(user_idx);
	}
	
	// word --> pdf
	@GetMapping(value = "/main2")
	public void main2() throws Exception {
		Document document = new Document("파일경로 + 파일 명 + 파일 확장자");
		document.save("저장할 경로 + 저장할 이름 + .pdf",SaveFormat.PDF);
	}
	
	// ppt, pptx --> pdf
	@GetMapping(value = "/main3")
	public void main3() throws Exception {
		Presentation presentation = new Presentation("파일경로 + 파일 명 + 파일 확장자");
		presentation.save("저장할 경로 + 저장할 이름 + .pdf", com.aspose.slides.SaveFormat.Pdf);
	}
	
}
