	package univ.together.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.FileDetailDto;
import univ.together.server.dto.FileDownloadDto;
import univ.together.server.dto.FileReserveDto;
import univ.together.server.dto.NewFileUploadDto;
import univ.together.server.model.File;
import univ.together.server.model.FileReservation;
import univ.together.server.model.FileVersion;

@Repository
@RequiredArgsConstructor
public class FileRepository {

	private final EntityManager em;
	
	public List<File> getFileList(Long project_idx) {
		return em.createQuery(
				"SELECT f FROM File f WHERE f.project_idx.project_idx = :project_idx AND f.com_delete_flag = :com_delete_flag",
				File.class).setParameter("project_idx", project_idx).setParameter("com_delete_flag", "N")
				.getResultList();
	}
	
	public List<File> getFileListV2(Long project_idx) {
		return em.createQuery("SELECT f FROM File f WHERE f.project_idx.project_idx = :project_idx AND " + 
							  "f.com_delete_flag = :com_delete_flag", File.class)
				.setParameter("project_idx", project_idx)
				.setParameter("com_delete_flag", "N")
				.getResultList();
	}
	
	public Map<Long, Long> getFileVersions(List<Long> file_idxes) {
		return em.createQuery("SELECT fv.file_idx.file_idx AS fileIdx, COUNT(fv) AS count FROM FileVersion fv " + 
							  "GROUP BY fv.file_idx.file_idx " + 
							  "HAVING fv.file_idx.file_idx IN (:file_idxes)", Tuple.class)
				.setParameter("file_idxes", file_idxes)
				.getResultStream()
				.collect(
					Collectors.toMap(
						tuple -> (Long) tuple.get("fileIdx"), 
						tuple -> (Long) tuple.get("count")
					)
				);
	}

	public List getFileVersionInfo(Long file_idx) {

		return em.createNativeQuery(//f.file_version_idx,
				"SELECT  f.file_modified_datetime, u.user_name, f.file_modified_comment FROM file_version f"
						+ " INNER JOIN user u ON u.user_idx = f.file_modified_member_idx WHERE f.file_idx= :file_idx")
				.setParameter("file_idx", file_idx).getResultList();
	}

	public String CheckFile(NewFileUploadDto uploadfiledto) {
		return em
				.createQuery(
						"SELECT f.file_origin_name FROM File f WHERE f.file_origin_name = :file_name AND"
								+ " f.file_extension = :file_extension AND f.project_idx.project_idx = :project_idx",
						String.class)
				.setParameter("file_name", uploadfiledto.getFile_origin_name())
				.setParameter("file_extension", uploadfiledto.getFile_extension())
				.setParameter("project_idx", uploadfiledto.getProject_idx()).getSingleResult();
	}

	public Long SaveFile(Long project_idx, File file) {
		em.createNativeQuery("INSERT INTO file(project_idx, file_origin_name,file_hashed_name"
				+ ", file_extension,file_path, file_type, file_upload_datetime, file_sema_flag) VALUE(:project_idx, :file_origin_name,:file_hashed_name,"
				+ ":file_extension, :file_path, :file_type,:file_upload_datetime,:file_sema_flag)")
				.setParameter("project_idx", project_idx).setParameter("file_origin_name", file.getFile_origin_name())
				.setParameter("file_hashed_name", file.getFile_hashed_name() + "v1")
				.setParameter("file_extension", file.getFile_extension()).setParameter("file_path", file.getFile_path())
				.setParameter("file_type", file.getFile_type())
				.setParameter("file_upload_datetime", file.getFile_upload_datetime())
				.setParameter("file_sema_flag", file.getFile_sema_flag()).executeUpdate();

		return em.createQuery("SELECT f.file_idx FROM File f ORDER BY f.file_idx DESC", Long.class).setFirstResult(0)
				.setMaxResults(1).getSingleResult();
	}

	// 최신 버전확인
	public Long getFileVersion(Long file_idx) {
		return em.createQuery(
				"SELECT COUNT(*) FROM FileVersion v WHERE v.file_idx.file_idx = :file_idx",
				Long.class).setParameter("file_idx", file_idx).setFirstResult(0).setMaxResults(1).getSingleResult();
	}

	public File getFileInfo(Long file_idx) {
		return em.createQuery("SELECT f FROM File f WHERE f.file_idx= :file_idx", File.class)
				.setParameter("file_idx", file_idx).getSingleResult();
	}

	public void addFileVersion(Long file_idx, Long user_idx, String comment) {

		em.createNativeQuery(
				"INSERT INTO file_version(file_modified_datetime, file_modified_member_idx, file_idx, file_modified_comment)"
						+ " VALUE(:file_modified_datetime, :file_modified_member_idx, :file_idx, :file_modified_comment)")
				.setParameter("file_modified_datetime", LocalDateTime.now())
				.setParameter("file_modified_member_idx", user_idx).setParameter("file_idx", file_idx)
				.setParameter("file_modified_comment", comment).executeUpdate();
	}
	// 파일 다운로드
	public FileDownloadDto fileDownload(Long file_idx) {

		File file = em.createQuery("SELECT f "
				+ "FROM File f INNER JOIN FileVersion v ON f.file_idx = v.file_idx  WHERE f.file_idx = :file_idx ORDER BY v.file_version_idx DESC",
				File.class).setParameter("file_idx", file_idx).setFirstResult(0).setMaxResults(1).getSingleResult();

		FileDownloadDto downloaddto = new FileDownloadDto(file.getFile_origin_name(), file.getFile_hashed_name(),
				file.getFile_extension(), file.getFile_path());
		return downloaddto;
	}
	

	public String getMemberRight(Long user_idx, Long project_idx) {

		return em.createQuery(
				"SELECT m.member_right FROM Member m WHERE m.user_idx.user_idx = :user_idx AND m.project_idx.project_idx = :project_idx",
				String.class).setParameter("user_idx", user_idx).setParameter("project_idx", project_idx)
				.getSingleResult();
	}

	// 파일삭제(팀원)
	public void deleteFileMember(Long file_idx, Long user_idx) {
		em.createNativeQuery("Update file f SET f.temp_delete_flag = 'Y' WHERE f.file_idx = :file_idx")
				.setParameter("file_idx", file_idx).executeUpdate();
		em.createNativeQuery("Update file f SET f.temp_delete_datetime = :datetime WHERE f.file_idx = :file_idx")
				.setParameter("file_idx", file_idx).setParameter("datetime", LocalDateTime.now()).executeUpdate();
		em.createNativeQuery("Update file f SET f.temp_delete_member_idx = :user_idx WHERE f.file_idx = :file_idx")
				.setParameter("file_idx", file_idx).setParameter("user_idx", user_idx).executeUpdate();
	}

	// 파일삭제(리더)
	public void deleteFileLeader(Long file_idx) {
		em.createNativeQuery("Update file f SET f.com_delete_flag = 'Y' WHERE f.file_idx = :file_idx")
				.setParameter("file_idx", file_idx).executeUpdate();
		em.createNativeQuery("Update file f SET f.com_delete_datetime = :datetime WHERE f.file_idx = :file_idx")
				.setParameter("file_idx", file_idx).setParameter("datetime", LocalDateTime.now()).executeUpdate();
	}

	// 파일 예약
	public String reserveFile(FileReserveDto filereservedto) {
		
		
		List num = em.createNativeQuery("SELECT COUNT(*) FROM file_reservation WHERE file_idx = :file_idx AND"
				+ " reserve_start_datetime <= :end_datetime AND reserve_end_datetime>= :start_datetime")
		.setParameter("file_idx", filereservedto.getFile_idx()).setParameter("start_datetime", filereservedto.getStart_datetime()).setParameter("end_datetime", filereservedto.getEnd_datetime())
		.getResultList();
		
		System.out.println(num.get(0).toString());
		if(num.get(0).toString()=="0") {
			em.createNativeQuery(
					"INSERT INTO file_reservation(user_idx, reserve_start_datetime, reserve_end_datetime, file_idx)"
							+ " VALUE(:user_idx, :reserve_start_datetime, :reserve_end_datetime, :file_idx)")
					.setParameter("user_idx", filereservedto.getUser_idx())
					.setParameter("reserve_start_datetime", filereservedto.getStart_datetime())
					.setParameter("reserve_end_datetime", filereservedto.getEnd_datetime())
					.setParameter("file_idx", filereservedto.getFile_idx()).executeUpdate();
			return "success";
		
		}
			return "err";
			
		
		
		
		
//		try {
//			dt = em.createQuery(
//					"SELECT r.reserve_start_datetime FROM FileReservation r WHERE r.file_idx.file_idx = :file_idx AND r.reserve_start_datetime"
//					+ "<= :end_datetime",
//					LocalDateTime.class).setParameter("file_idx", filereservedto.getFile_idx())
//					.setParameter("end_datetime", filereservedto.getEnd_datetime())
//					.getSingleResult();
//			
//			em.createNativeQuery(
//					"INSERT INTO file_reservation(user_idx, reserve_start_datetime, reserve_end_datetime, file_idx)"
//							+ " VALUE(:user_idx, :reserve_start_datetime, :reserve_end_datetime, :file_idx)")
//					.setParameter("user_idx", filereservedto.getUser_idx())
//					.setParameter("reserve_start_datetime", filereservedto.getStart_datetime())
//					.setParameter("reserve_end_datetime", dt.minusMinutes(1))
//					.setParameter("file_idx", filereservedto.getFile_idx()).executeUpdate();
//
//			
//		} catch (Exception e) {
//			System.out.println(e);
//		em.createNativeQuery(
//				"INSERT INTO file_reservation(user_idx, reserve_start_datetime, reserve_end_datetime, file_idx)"
//						+ " VALUE(:user_idx, :reserve_start_datetime, :reserve_end_datetime, :file_idx)")
//				.setParameter("user_idx", filereservedto.getUser_idx())
//				.setParameter("reserve_start_datetime", filereservedto.getStart_datetime())
//				.setParameter("reserve_end_datetime", filereservedto.getEnd_datetime())
//				.setParameter("file_idx", filereservedto.getFile_idx()).executeUpdate();
//		}
	}

	// 파일 예약 메인
	public List<FileReservation> reserveFileList(Long file_idx) {
//		List l = new ArrayList<>();
		
		return em.createQuery("SELECT fr FROM FileReservation fr JOIN FETCH fr.user_idx WHERE fr.file_idx.file_idx = :file_idx ORDER BY fr.reserve_start_datetime", FileReservation.class)
				.setParameter("file_idx", file_idx)
				.getResultList();
		
//		try {

//		return em.createNativeQuery("SELECT * FROM file_reservation r " + "WHERE r.file_idx = :file_idx",
//				FileReservation.class).setParameter("file_idx", file_idx).getResultList();

//			System.out.println(e);
//			return e;
//		} catch (Exception e) {
//			return l;
//		}

	}// 파일 이름보내주기

	// 파일 예약 삭제
	public String deleteFileReservation(Long file_reservation_idx) {
		try {
			em.createNativeQuery("DELETE FROM file_reservation r WHERE r.file_reservation_idx = :file_reservation_idx ")
					.setParameter("file_reservation_idx", file_reservation_idx).executeUpdate();
			System.out.println("214124215125125125125125211");
			return "success";
		} catch (Exception e) {
			System.out.println(e);
			return "failed";
		}
	}

//파일 상세
	public FileDetailDto getFileDetail(Long file_idx) {
		System.out.println("1");
		int file_reservation_flag;
		FileReservation fr = null;
		
		String file_type = em.createQuery("SELECT f.file_type FROM File f WHERE f.file_idx = :file_idx",String.class)
				.setParameter("file_idx", file_idx)
				.getSingleResult();
		try {
			fr = em.createQuery(
					"SELECT r FROM FileReservation r WHERE r.file_idx.file_idx = :file_idx AND r.reserve_start_datetime"
							+ "<= :now_datetime1 AND r.reserve_end_datetime >= : now_datetime2",
					FileReservation.class).setParameter("file_idx", file_idx)
					.setParameter("now_datetime1", LocalDateTime.now())
					.setParameter("now_datetime2", LocalDateTime.now()).getSingleResult();
			file_reservation_flag = 1;
		} catch (Exception e) {
			System.out.println("1231412542");
			file_reservation_flag = 0;
		}

		FileVersion fileversion = em.createQuery(
				"SELECT v FROM FileVersion v WHERE v.file_idx.file_idx =: file_idx ORDER BY v.file_version_idx DESC",
				FileVersion.class).setParameter("file_idx", file_idx).setFirstResult(0).setMaxResults(1)
				.getSingleResult();
		System.out.println("2");
		String delete_member_name="";
		try {
		delete_member_name = em
				.createQuery("SELECT f.temp_delete_member_idx.user_name FROM File f WHERE f.file_idx = :file_idx", String.class)
				.setParameter("file_idx", file_idx).getSingleResult();
		}catch(Exception e) {
			delete_member_name = "";
		}
		System.out.println(delete_member_name);
		String delete_flag = em
				.createQuery("SELECT f.temp_delete_flag FROM File f WHERE f.file_idx = :file_idx", String.class)
				.setParameter("file_idx", file_idx).getSingleResult();
		System.out.println("3");
		String u_vname = getUserName(fileversion.getFile_modified_member_idx().getUser_idx());
		if (file_reservation_flag == 1) {
			String u_rname = getUserName(fr.getUser_idx().getUser_idx());
			FileDetailDto filedetaildto = new FileDetailDto(u_vname, fileversion.getFile_modified_datetime(),
					fileversion.getFile_modified_comment(), delete_member_name, delete_flag, fr.getUser_idx().getUser_idx(), u_rname,
					fr.getReserve_start_datetime(), fr.getReserve_end_datetime(), file_reservation_flag,file_type);
			return filedetaildto;

		} // 예약시간일때
		
		//예약이 안된시간일때	
//		int file_sema_flag = em.createQuery("SELECT f.file_sema_flag FROM File f WHERE f.file_idx = : file_idx", int.class)
//				.setParameter("file_idx", file_idx)
//				.getSingleResult();
//		
	
		try {//가장 최근 예약 불러움
			fr = em.createQuery(
					"SELECT r FROM FileReservation r WHERE r.file_idx.file_idx = :file_idx AND"
							+ " r.reserve_start_datetime > :now_datetime ORDER BY r.reserve_start_datetime",
					FileReservation.class).setParameter("file_idx", file_idx)
					.setParameter("now_datetime", LocalDateTime.now()).setMaxResults(1).getSingleResult();

			String u_rname = getUserName(fr.getUser_idx().getUser_idx());
			
			FileDetailDto filedetaildto = new FileDetailDto(u_vname, fileversion.getFile_modified_datetime(),
					fileversion.getFile_modified_comment(), delete_member_name,delete_flag, fr.getUser_idx().getUser_idx(), u_rname,
					fr.getReserve_start_datetime(), fr.getReserve_end_datetime(), file_reservation_flag,file_type);
			return filedetaildto;
		} catch (Exception e) {//아에 예약이 텅텅 비었음
			String u_rname = "";
			Long n = (long) 0;
			FileDetailDto filedetaildto = new FileDetailDto(u_vname, fileversion.getFile_modified_datetime(),
					fileversion.getFile_modified_comment(), delete_member_name,delete_flag, n, u_rname, LocalDateTime.now(),
					LocalDateTime.now(), file_reservation_flag, file_type);
			return filedetaildto;
		}
	}

	public String getUserName(Long user_idx) {
		return em.createQuery("SELECT u.user_name FROM User u WHERE u.user_idx = : user_idx", String.class)
				.setParameter("user_idx", user_idx).getSingleResult();
	}

	
	// 파일예약조기종료
	public String earlyFinish(Long file_idx) {
		Long file_reservation_idx = em.createQuery(
				"SELECT r.file_reservation_idx FROM FileReservation r WHERE r.reserve_start_datetime <= :now_datetime AND"
						+ " r.reserve_end_datetime >= : now_datetime AND r.file_idx.file_idx = :file_idx",Long.class)
				.setParameter("now_datetime", LocalDateTime.now()).setParameter("file_idx", file_idx).getSingleResult();
		em.createNativeQuery("UPDATE file_reservation SET reserve_end_datetime = :now WHERE file_reservation_idx"
				+ "= :file_reservation_idx")
		.setParameter("now", LocalDateTime.now().minusMinutes(1))
		.setParameter("file_reservation_idx", file_reservation_idx)
		.executeUpdate();
		
		return "success";
	}
	
	// 파일이 예약되어있는가?-> 예 -> 파일 접근 권한 확인(예약자인가) -> 예 -> 업로드 다운로드 기능 모두다 가능
	// -> 아니오 -> 다운로드 기능
	// -> 아니오 -> 파일이 수정중인가? (sema_Flag) -> 아니오 -> 업로드 다운로드 기능 모두다 가능
	// -> 예 -> 다운로드 가능
}
