package se.lexicon.g55springail2.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.g55springail2.service.ChatService;
import se.lexicon.g55springail2.service.OpenAIChatService;

@RestController
@RequestMapping("/api/chat")
@Validated
public class ChatController {

    private OpenAIChatService service;

    private ChatService chatService;

    @Autowired
    public ChatController(OpenAIChatService service, ChatService chatService) {
        this.service = service;
        this.chatService = chatService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam
                      @NotNull(message = "Question cannot be null")
                      @NotBlank(message = "Question cannot be blank")
                      @Size(max = 200, message = "Question cannot exceed 200 characters")
                      String question) {
        System.out.println("question = " + question);
        return service.processSimpleChatQuery(question);
    }

    @GetMapping("/ask/memory")
    public String askMemory(@RequestParam
                            @NotNull(message = "Question cannot be null")
                            @NotBlank(message = "Question cannot be blank")
                            @Size(max = 200, message = "Question cannot exceed 200 characters")
                            String question,
                            @NotNull(message = "ConversationId cannot be null")
                            @NotBlank(message = "ConversationId cannot be blank")
                            @Size(max = 200, message = "ConversationId cannot exceed 200 characters")
                            String conversationId) {
        System.out.println("question = " + question);
        System.out.println("conversationId = " + conversationId);
        return service.chatMemory(question, conversationId);
    }


    @GetMapping("/ask/new-memory")
    public String askChatMemory(@RequestParam
                            @NotNull(message = "Question cannot be null")
                            @NotBlank(message = "Question cannot be blank")
                            @Size(max = 200, message = "Question cannot exceed 200 characters")
                            String question,
                            @NotNull(message = "ConversationId cannot be null")
                            @NotBlank(message = "ConversationId cannot be blank")
                            @Size(max = 200, message = "ConversationId cannot exceed 200 characters")
                            String conversationId) {
        System.out.println("question = " + question);
        System.out.println("conversationId = " + conversationId);
        return chatService.chatMemory(question, conversationId);
    }
}
