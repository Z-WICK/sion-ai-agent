package com.sion.sionaiagent.app;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;


    @Test
    void doChat() {
        String UUID = StrUtil.uuid();

        String message = "你好";
        String answer = loveApp.doChat(message, UUID);
        Assertions.assertNotNull(answer);

        message = "我的女朋友叫黑手神鹰";
        answer = loveApp.doChat(message, UUID);
        Assertions.assertNotNull(answer);

        message = "他要跟我分手怎么办";
        answer = loveApp.doChat(message, UUID);
        Assertions.assertNotNull(answer);


    }

    @Test
    void doChatWithReport() {
        String UUID = StrUtil.uuid();

        String message = "你好,我是牛马程序员，我的女朋友最近跟我提分手我不知道怎么办好了";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, UUID);
        Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我不知道我适不适合谈恋爱";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

}