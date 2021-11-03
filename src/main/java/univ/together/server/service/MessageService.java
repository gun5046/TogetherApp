package univ.together.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MessageHistoryDto;
import univ.together.server.dto.SendMessageDto;
import univ.together.server.repository.MessageRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

	private final MessageRepository messageRepository;
	
	public List<MessageHistoryDto> getMessageHistory(Long project_idx) {
		// 해당 단톡방의 채팅들을 가져온다.
		return messageRepository.getMessageHistory(project_idx).stream().map(m -> new MessageHistoryDto(m)).collect(Collectors.toList());
	}
	
	@Transactional
	public SendMessageDto saveChatMessage(SendMessageDto sendMessageDto, Long project_idx) {
		SendMessageDto sentMessage = new SendMessageDto();
		try {
			int rowNum = messageRepository.saveChatMessage(sendMessageDto, project_idx);
			if(rowNum != 1) throw new Exception();
			sentMessage = new SendMessageDto(messageRepository.getLastSendMessage(sendMessageDto.getUser_idx()));
			if(sentMessage != null) sentMessage.setCode("success");
		}catch(Exception e) {
			sentMessage.setCode("fail");
		}
		return sentMessage;
	}
	
}
