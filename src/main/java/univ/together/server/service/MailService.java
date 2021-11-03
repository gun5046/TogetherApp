package univ.together.server.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

// 메일을 보내기 위한 서비스를 따로 뺀겁니다.
@Service
@RequiredArgsConstructor
public class MailService implements EmailUtil {

	private final JavaMailSender sender;
	
	@Override
	public void sendEmail(String toAddress, String subject, String body) {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		try {
			helper.setTo(toAddress);
			helper.setSubject(subject);
			helper.setText(body);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		sender.send(message);
		
	}

}