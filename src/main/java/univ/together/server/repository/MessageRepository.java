package univ.together.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.SendMessageDto;
import univ.together.server.model.ChatMessage;

@Repository
@RequiredArgsConstructor
public class MessageRepository {

	private final EntityManager em;
	
	public List<ChatMessage> getMessageHistory(Long project_idx) {
		return em.createQuery("SELECT c FROM ChatMessage c " + 
					"JOIN FETCH c.writer_idx " + 
					"WHERE c.project_idx.project_idx = :project_idx ORDER BY c.chat_message_idx ASC", ChatMessage.class)
				.setParameter("project_idx", project_idx)
				.getResultList();
	}
	
	public int saveChatMessage(SendMessageDto sendMessageDto, Long project_idx) {
		return em.createNativeQuery("INSERT INTO chat_message " + 
					"(project_idx, writer_idx, chat_message_body, send_datetime) VALUES " + 
					"(:project_idx, :writer_idx, :chat_message_body, :send_datetime)")
				.setParameter("project_idx", project_idx)
				.setParameter("writer_idx", sendMessageDto.getUser_idx())
				.setParameter("chat_message_body", sendMessageDto.getChat_message_body())
				.setParameter("send_datetime", LocalDateTime.now())
				.executeUpdate();
	}
	
	public ChatMessage getLastSendMessage(Long user_idx) {
		return em.createQuery("SELECT c FROM ChatMessage c JOIN FETCH c.writer_idx " + 
					"WHERE c.writer_idx.user_idx = :user_idx ORDER BY c.chat_message_idx DESC", ChatMessage.class)
				.setParameter("user_idx", user_idx)
				.setFirstResult(0)
				.setMaxResults(1)
				.getSingleResult();
	}
	
}
