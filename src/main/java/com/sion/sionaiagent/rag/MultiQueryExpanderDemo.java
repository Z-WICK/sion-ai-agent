package com.sion.sionaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import javax.annotation.processing.FilerException;
import java.util.List;

/**
 * @Author : wick
 * @Date : 2025/6/18 17:02
 */
@Component
public class MultiQueryExpanderDemo {

    @Resource
    private VectorStore pgVectorVectorStore;

    private final ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel openAiChatModel) {
        this.chatClientBuilder = ChatClient.builder(openAiChatModel);
    }


    public List<Query> expand(String query) {
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();

        List<Query> queries = queryExpander.expand(new Query(query));
        return queries;
    }

    // 扩展查询
    public void expandQuery(Query query) {

        DocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(pgVectorVectorStore)
                .similarityThreshold(0.73)
                .topK(3)
                .build();
        List<Document> retrievedDocuments = retriever.retrieve(query);
        System.out.println(retrievedDocuments);
    }



}
