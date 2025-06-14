package com.sion.sionaiagent.demo.invoke;

   import jakarta.annotation.Resource;
   import org.springframework.ai.chat.messages.AssistantMessage;
   import org.springframework.ai.chat.model.ChatModel;
   import org.springframework.ai.chat.prompt.Prompt;
   import org.springframework.boot.CommandLineRunner;
   import org.springframework.stereotype.Component;

   /**
    * @Author : wick
    * @Date : 2025/5/21 16:33
    */
   @Component
   public class SpringOpenAiInvoke implements CommandLineRunner {

       @Resource(name = "openAiChatModel")
       private ChatModel chatModel; // 直接使用 OpenAI 的 ChatModel

       @Override
       public void run(String... args) throws Exception {
           AssistantMessage output = chatModel.call(new Prompt("你好"))
                   .getResult().getOutput();
           System.out.println(output.getText());
       }
   }