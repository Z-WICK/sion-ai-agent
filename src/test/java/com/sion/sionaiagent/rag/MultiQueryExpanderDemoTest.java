package com.sion.sionaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void expand() {
        List<Query> queries = multiQueryExpanderDemo.expand("又幻想在不久的将来能谈上一个甜妹了");
        Query query = queries.get(1);
        multiQueryExpanderDemo.expandQuery(query);
        Assertions.assertNotNull(queries);
    }


    @Test
    void expandQuery() {

    }
}