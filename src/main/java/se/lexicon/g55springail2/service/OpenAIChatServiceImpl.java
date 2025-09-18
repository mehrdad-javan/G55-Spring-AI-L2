package se.lexicon.g55springail2.service;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAIChatServiceImpl implements OpenAIChatService {

    private OpenAiChatModel openAiChatModel;

    private ChatMemory chatMemory;

    @Autowired
    public OpenAIChatServiceImpl(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {
        this.openAiChatModel = openAiChatModel;
        this.chatMemory = chatMemory;
    }

    @Override
    public String processSimpleChatQuery(final String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        return openAiChatModel.call(query);
    }

    public String chatMemory(final String query, final String conversationId) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
        if (conversationId == null || conversationId.trim().isEmpty()) {
            throw new IllegalArgumentException("ConversationId cannot be null or empty");
        }

        /*if (chatMemory.get(conversationId).size() >= 4) {
            return "You have reached the maximum number of messages in this conversation. Please start a new conversation.";
        }*/

        UserMessage userMessage = UserMessage.builder()
                .text(query)
                .build();
        chatMemory.add(conversationId, userMessage);

        Prompt prompt = Prompt.builder()
                .messages(chatMemory.get(conversationId))
                .chatOptions(OpenAiChatOptions.builder()
                        .model("gpt-4.1-mini")
                        .temperature(0.2)
                        .maxTokens(500)
                        .build())
                .build();

        ChatResponse chatResponse = openAiChatModel.call(prompt);
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        System.out.println("Current memory size: " + chatMemory.get(conversationId).size());
        System.out.println("Messages in memory:");
        chatMemory.get(conversationId).forEach(msg -> System.out.println(msg.getText()));

        return chatResponse.getResult().getOutput().getText();


    }

    public void resetChat(String conversationId) {
        chatMemory.clear(conversationId);
    }

}
