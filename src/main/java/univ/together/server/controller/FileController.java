package univ.together.server.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.DeleteFileDto;
import univ.together.server.dto.FileDetailDto;
import univ.together.server.dto.FileDownloadDto;
import univ.together.server.dto.FileListDto;
import univ.together.server.dto.FileReserveDto;
import univ.together.server.dto.GetReservationListDto;
import univ.together.server.dto.NewFileUploadDto;
import univ.together.server.dto.VersionUploadDto;
import univ.together.server.service.FileService;

@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	// 파일 메인 리스트
	@GetMapping(value = "/main")
	public List<FileListDto> main(@RequestParam(name = "project_idx") Long project_idx) {
		return fileService.getFileList(project_idx);
	}

	// 피일 버전 상세
	@GetMapping(value = "/version/{file_idx}")
	public List getFileVersionInfo(@PathVariable(name = "file_idx") Long file_idx) {
		return fileService.getFileVersionInfo(file_idx);
	}

	// 새파일 업로드
	@PostMapping(value = "/uploadNew", consumes = { "multipart/form-data" }) // 체크 파일 & 업로드 파일 (project_idx,
																				// file_origin_name, file_extension,
																				// file_type)
	public String newFileUpload(@ModelAttribute NewFileUploadDto fileuploaddto) {
		if (fileService.checkFile(fileuploaddto).equals("existed")) {
			return "existed";
		}
		return fileService.saveFile(fileuploaddto);
	}

	// 수정 & 다운로드
	@GetMapping(value = "/detail/download")
	public ResponseEntity<Resource> fileModifyAndDownload(@RequestParam("file_idx") Long file_idx, @RequestParam("user_idx") Long user_idx) throws IOException {// file_idx->1
		FileDownloadDto filedownloaddto = fileService.fileModifyAndDownload(file_idx, user_idx);
		
		Path path = Paths.get(filedownloaddto.getFile_path()+ "/" + filedownloaddto.getFile_hashed_name()+ "." + filedownloaddto.getFile_extension());
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
						+ filedownloaddto.getFile_origin_name() + "." + filedownloaddto.getFile_extension() + "\"")
				.body(resource);
	}

	// 읽기 & 다운로드
	@GetMapping(value ="/detail/download/read/{file_idx}")
	public ResponseEntity<Resource> fileReadAndDownload(@PathVariable("file_idx") Long file_idx) throws IOException {// file_idx->1
		FileDownloadDto filedownloaddto = fileService.fileReadAndDownload(file_idx); // 파일 읽기 다운로드 구현
		Path path = Paths.get(filedownloaddto.getFile_path()+ "/" + filedownloaddto.getFile_hashed_name()+ "." + filedownloaddto.getFile_extension());
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
						+ filedownloaddto.getFile_origin_name() + "." + filedownloaddto.getFile_extension() + "\"")
				.body(resource);
	} 
	
	//예약한사람 파일 다운로드

	@GetMapping(value ="/detail/download/reserve/{file_idx}")
	public ResponseEntity<Resource> fileDownload(@PathVariable("file_idx") Long file_idx) throws IOException {// file_idx->1
		FileDownloadDto filedownloaddto = fileService.fileReadAndDownload(file_idx); // 파일 읽기 다운로드 구현
		Path path = Paths.get(filedownloaddto.getFile_path()+ "/" + filedownloaddto.getFile_hashed_name()+ "." + filedownloaddto.getFile_extension());
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
						+ filedownloaddto.getFile_origin_name() + "." + filedownloaddto.getFile_extension() + "\"")
				.body(resource);
	}
	
	// 파일 버전 업로드
	@PostMapping(value = "/detail/uploadVersion", consumes = { "multipart/form-data" })
	public String addFileVersion(@ModelAttribute VersionUploadDto versionuploaddto) {
		return fileService.addFileVersion(versionuploaddto);
	}

	// 파일삭제
	@PostMapping(value = "/detail/deleteFile")
	public String deleteFile(@RequestBody DeleteFileDto deletefiledto) {
		return fileService.deleteFile(deletefiledto);
	}

	// 파일예약메인
	@GetMapping(value = "/detail/reserveFileList/{file_idx}")
	public List<GetReservationListDto> reservationList(@PathVariable("file_idx") Long file_idx) {
		return fileService.reserveFileList(file_idx);
	}

	// 파일예약
	@PostMapping(value = "/detail/reserveFile")
	public String reserveFile(@RequestBody FileReserveDto filereservedto) {
		return fileService.reserveFile(filereservedto);
	}

	// 파일예약삭제
	@GetMapping(value = "/detail/deleteFileReservation/{file_reservation_idx}")
	public String deleteFileReservation(@PathVariable(name = "file_reservation_idx") Long file_reservation_idx) {
		return fileService.deleteFileReservation(file_reservation_idx);
	}

	// 파일상세
	@GetMapping(value = "/detail/{file_idx}")
	public FileDetailDto getFileDetail(@PathVariable(name = "file_idx") Long file_idx) {
		return fileService.getFileDetail(file_idx);
	}

	// 파일예약조기종료
	@GetMapping(value = "/detail/earlyFinish/{file_idx}")
	public String EarlyFinish(@PathVariable("file_idx") Long file_idx) {
		return fileService.earlyFinish(file_idx);
	}

	// 버전 되돌리기
	@GetMapping(value = "/detail/return")
	public String returnToVersion(@RequestParam("file_idx") Long file_idx, @RequestParam("file_version_idx") Long file_version_idx
			, @RequestParam("user_idx") Long user_idx) {
		return fileService.returnToVersion(file_idx, file_version_idx, user_idx);
	}
}
