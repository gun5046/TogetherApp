package univ.together.server.service;

// 이메일을 보내기 위한 인터페이스입니다.
public interface EmailUtil {
	void sendEmail(String toAddress, String subject, String body);
}
