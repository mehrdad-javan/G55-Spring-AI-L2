package se.lexicon.g55springail2.service;

public interface OpenAIChatService {

    String processSimpleChatQuery(final String query);

    String chatMemory(final String query, final String conversationId);
}
