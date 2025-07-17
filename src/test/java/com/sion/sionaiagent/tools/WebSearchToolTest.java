package com.sion.sionaiagent.tools;

import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class WebSearchToolTest {

    @Value("$(search-api.api-key)")
    private String searchApiKey;

    @Test
    public void testSearchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        String query = "What is the capital of France?";
        String result = webSearchTool.searchWeb(query);
        assertNotNull(result);
    }
}