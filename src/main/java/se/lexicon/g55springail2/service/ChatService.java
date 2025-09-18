package se.lexicon.g55springail2.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private ChatClient chatClient;
    private ChatMemory chatMemory;
    private AppToolCalling appToolCalling;

    public ChatService(ChatClient.Builder chatClient, ChatMemory chatMemory, AppToolCalling appToolCalling) {
        this.chatClient = chatClient
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        // config the vector database
                ).build();
        this.chatMemory = chatMemory;
        this.appToolCalling = appToolCalling;
    }

    public String chatMemory(final String query, final String conversationId) {
        if (query == null || conversationId == null) {
            throw new IllegalArgumentException("Query and ConversationId cannot be null");
        }

        ChatResponse chatResponse = chatClient.prompt()
                .system("""
                        You are a specialized name management assistant with the following capabilities:
                        1. You can fetch and display all stored names using the 'fetAllNames' tool
                        2. You can search for specific names using the 'findByName' tool
                        3. You can add new names using the 'addNewName' tool
                        
                        Guidelines:
                        - Always use the appropriate tool for name-related operations
                        - Only respond with name-related information
                        - If a request is not about names, politely explain that you can only help with name management
                        - When displaying names, present them in a clear, organized manner
                        - Confirm successful operations with brief, clear messages
                        """)
                .user(query)
                .tools(appToolCalling)
                .options(OpenAiChatOptions.builder().temperature(0.2).maxTokens(1000).build())
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }


}
