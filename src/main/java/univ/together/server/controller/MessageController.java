package univ.together.server.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import univ.together.server.dto.MessageHistoryDto;
import univ.together.server.dto.SendMessageDto;
import univ.together.server.service.MessageService;

@RestController
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;
	
	@MessageMapping(value = "/message/{project_idx}")
	@SendTo(value = "/return/message/{project_idx}")
	public SendMessageDto sendMessage(@DestinationVariable("project_idx") Long project_idx, 
							@RequestBody SendMessageDto sendMessageDto) throws Exception {
		return messageService.saveChatMessage(sendMessageDto, project_idx);
	}
	
	@GetMapping(value = "/message/chatroom")
	public List<MessageHistoryDto> getMessageHistory(@RequestParam(name = "project_idx") Long project_idx) {
		return messageService.getMessageHistory(project_idx);
	}
	
}
