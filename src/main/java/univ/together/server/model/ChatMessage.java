package univ.together.server.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CHAT_MESSAGE")
@Getter
@Setter(value=AccessLevel.PRIVATE)
public class ChatMessage {
	
	@Id
	@Column(name = "CHAT_MESSAGE_IDX")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long chat_message_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECT_IDX")
	private Project project_idx;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WRITER_IDX")
	private User writer_idx;
	
	@Column(name = "CHAT_MESSAGE_BODY")
	private String chat_message_body;
	
	@Column(name = "SEND_DATETIME")
	private LocalDateTime send_datetime;
	
}
