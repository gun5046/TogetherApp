package univ.together.server.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.DeleteFileDto;
import univ.together.server.dto.FileDetailDto;
import univ.together.server.dto.FileDownloadDto;
import univ.together.server.dto.FileListDto;
import univ.together.server.dto.FileReserveDto;
import univ.together.server.dto.GetReservationListDto;
import univ.together.server.dto.NewFileUploadDto;
import univ.together.server.dto.VersionUploadDto;
import univ.together.server.model.File;
import univ.together.server.repository.FileRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
	private final FileRepository fileRepository;
	private final PasswordEncoder passwordEncoder;

	public List<FileListDto> getFileList(Long project_idx) {
//		return fileRepository.getFileList(project_idx).stream().map(f -> new FileListDto(f))
//				.collect(Collectors.toList());
		
		// 파일 리스트
		List<File> files = fileRepository.getFileListV2(project_idx);
		// file_idx리스트
		List<Long> file_idxes = new ArrayList<Long>();
		
		for(int i = 0; i < files.size(); i++) file_idxes.add(files.get(i).getFile_idx());
		
		// return 해줄 DTO리스트
		List<FileListDto> fileListDtos = new ArrayList<FileListDto>();
		// 파일 리스트에 맞는 파일 버전 리스트
		Map<Long, Long> versions = fileRepository.getFileVersions(file_idxes);
		
		if(files.size() == versions.size()) {
			for(int i = 0; i < files.size(); i++) {
				fileListDtos.add(new FileListDto(files.get(i), versions.get(files.get(i).getFile_idx())));
			}
		}
		
		return fileListDtos;
	}

	public List getFileVersionInfo(Long file_idx) {
		return fileRepository.getFileVersionInfo(file_idx);
	}

	// 업로드하는 파일 저장
	@Transactional
	public String saveFile(NewFileUploadDto uploadfiledto) {
		String file_hashed_name = passwordEncoder.encode(uploadfiledto.getFile_origin_name());
		// 파일 확장자
		String file_extension = uploadfiledto.getFile_extension();
		String file_path = "/usr/local/Together/" + uploadfiledto.getProject_idx();
		String file_name = file_hashed_name + "v1" + "." + uploadfiledto.getFile_extension();

//		java.nio.file.Files folder = new java.nio.file.Files(file_path);
		java.io.File folder = new java.io.File(file_path, file_name);

		try {
			folder.mkdirs();// 폴더 생성합니다.
			System.out.println("폴더 생성");

		} catch (Exception e) {
			System.out.println("폴더 생성 X");
			return "folderError";
		}
//		java.io.File newFile = new java.io.File(file_path, file_hashed_name +"v1"+ "." + uploadfiledto.getFile_extension());
		try {
			uploadfiledto.getMultipartfile().transferTo(folder);
		} catch (Exception e) {
			return "failed";
		}

//	try
//	{
//	newFile.createNewFile();
//		uploadfiledto.getMultipartfile().transferTo(newFile);
//	}catch(
//	IOException e)
//	{

		File file = File.builder().file_origin_name(uploadfiledto.getFile_origin_name())
				.file_hashed_name(file_hashed_name).file_extension(file_extension).file_path(file_path)
				.file_type(uploadfiledto.getFile_type()).build();
		System.out.println("dddd");
		System.out.println(uploadfiledto.getUser_idx());
		Long fid = fileRepository.SaveFile(uploadfiledto.getProject_idx(), file);
		String comment = "새로 생성";
		fileRepository.addFileVersion(fid, uploadfiledto.getUser_idx(), comment);

		return "success";

	}

	// 새 파일 업로드 시 파일 이름 중복 확인, 파일 확장자 확인
	public String checkFile(NewFileUploadDto uploadfiledto) {
		try {
			if (fileRepository.CheckFile(uploadfiledto).equals(uploadfiledto.getFile_origin_name())) {
				System.out.println("Failed");
				return "existed";
			}
			return "success";
		} catch (Exception e) {
			return "success";
		}
	}

	// 파일 새 버전 업로드
	@Transactional
	public String addFileVersion(VersionUploadDto versionuploaddto) {
		File file = fileRepository.getFileInfo(versionuploaddto.getFile_idx());
		Long file_version_idx = fileRepository.getFileVersion(versionuploaddto.getFile_idx()) + 1;
		String file_path = file.getFile_path();
		String file_name = file.getFile_hashed_name().substring(0, file.getFile_hashed_name().lastIndexOf("v1")) + "v"
				+ file_version_idx + "." + file.getFile_extension();
		System.out.println(file_path);
		java.io.File filevs = new java.io.File(file_path, file_name);

		try {
			filevs.mkdirs();// 폴더 생성합니다.
			System.out.println("폴더 생성");

		} catch (Exception e) {
			System.out.println("폴더 생성 X");
		}

		try {
			versionuploaddto.getMultipartfile().transferTo(filevs);
		} catch (Exception e) {
			return "failed";
		}

		fileRepository.addFileVersion(versionuploaddto.getFile_idx(), versionuploaddto.getUser_modified_idx(),
				versionuploaddto.getFile_modified_comment());

		return "success";
	}

	//파일 읽기 & 다운로드
	@Transactional
	public FileDownloadDto fileReadAndDownload(Long file_idx) {
		return fileRepository.fileDownload(file_idx);
	}
	
	//파일 수정 & 다운로드
	@Transactional
	public FileDownloadDto fileModifyAndDownload(Long file_idx, Long user_idx) {
		
		FileReserveDto reservedto = new FileReserveDto(file_idx, user_idx, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
		reserveFile(reservedto);
		return fileRepository.fileDownload(file_idx);
	}

	// 파일 삭제
	@Transactional
	public String deleteFile(DeleteFileDto deletefiledto) {
		String member_right = fileRepository.getMemberRight(deletefiledto.getUser_idx(),
				deletefiledto.getProject_idx());
		try {
			if (member_right.equals("Leader") || member_right.equals("Admin")) {
				fileRepository.deleteFileLeader(deletefiledto.getFile_idx());
			} else {
				fileRepository.deleteFileMember(deletefiledto.getFile_idx(), deletefiledto.getUser_idx());
			}
			return member_right;
		} catch (Exception e) {
			System.out.println(e);
			return "failed";
		}
	}

	// 파일 예약
	@Transactional
	public String reserveFile(FileReserveDto filereservedto) {
		
			return fileRepository.reserveFile(filereservedto);
		
	}

	// 파일 예약 메인
	public List<GetReservationListDto> reserveFileList(Long file_idx) {
		return fileRepository.reserveFileList(file_idx).stream().map(fr -> new GetReservationListDto(fr)).collect(Collectors.toList());
	}

	// 파일 예약 삭제
	@Transactional
	public String deleteFileReservation(Long file_reservation_idx) {
		return fileRepository.deleteFileReservation(file_reservation_idx);
	}

	// 파일 상세
	public FileDetailDto getFileDetail(Long file_idx) {
		return fileRepository.getFileDetail(file_idx);
	}

	// 파일예약조기종료
	@Transactional
	public String earlyFinish(Long file_idx) {
		return fileRepository.earlyFinish(file_idx);
	}

	// 버전되돌리기
	@Transactional
	public String returnToVersion(Long file_idx, Long file_version_idx,Long user_idx) {
		System.out.println("!23124");
		File file = fileRepository.getFileInfo(file_idx);
		String file_path = file.getFile_path();
		String file_name = file.getFile_hashed_name().substring(0, file.getFile_hashed_name().lastIndexOf("v1")) + "v"
				+ file_version_idx + "." + file.getFile_extension();

		Long newfile_version_idx = fileRepository.getFileVersion(file.getFile_idx()) + 1;
		String newfile_name = file.getFile_hashed_name().substring(0, file.getFile_hashed_name().lastIndexOf("v1"))
				+ "v" + newfile_version_idx + "." + file.getFile_extension();

		System.out.println(newfile_version_idx);
		System.out.println(file_path);
		java.io.File filevs = new java.io.File(file_path, file_name);
		java.io.File newfilevs = new java.io.File(file_path, newfile_name);

//		try {
//			newfilevs.mkdirs();// 폴더 생성합니다.
//			System.out.println("폴더 생성");
//
//		} catch (Exception e) {
//			System.out.println("폴더 생성 X");
//		}

		try {

			FileInputStream fis = new FileInputStream(filevs); // 원
			FileOutputStream fos = new FileOutputStream(newfilevs); // 복

			int fileByte = 0;
			// fis.read()가 -1 이면 파일을 다 읽음
			while ((fileByte = fis.read()) != -1) {
				fos.write(fileByte);
			}
			// 자원사용종료
			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fileRepository.addFileVersion(file_idx, user_idx,"v"+file_version_idx+" 되돌림");


		return "success";
	}

}