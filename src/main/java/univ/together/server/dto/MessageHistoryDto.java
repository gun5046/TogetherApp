package univ.together.server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import univ.together.server.model.ChatMessage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageHistoryDto {
	
	private String user_name;
	private Long user_idx;
	private String user_profile_photo;
	private String chat_message_body;
	private LocalDateTime send_datetime;
	
	public MessageHistoryDto(ChatMessage chatMessage) {
		this.user_name = chatMessage.getWriter_idx().getUser_name();
		this.user_idx = chatMessage.getWriter_idx().getUser_idx();
		this.user_profile_photo = chatMessage.getWriter_idx().getUser_profile_photo();
		this.chat_message_body = chatMessage.getChat_message_body();
		this.send_datetime = chatMessage.getSend_datetime();
	}
	
}
