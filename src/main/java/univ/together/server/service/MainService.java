package univ.together.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.ProjectListDto;
import univ.together.server.repository.MainRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainService {

	private final MainRepository mainRepository;
	
	public List<ProjectListDto> findProjectByUserIdx(Long user_idx) {
		
		// 유저 idx를 통해 속한 프로젝트들의 정보(이름, 멤버 수)를 가져온다.
		return mainRepository.findProjectByUserIdx(user_idx).stream().map(m -> new ProjectListDto(m)).collect(Collectors.toList());
		
	}
	
}
