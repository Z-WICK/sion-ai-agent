package com.sion.sionaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.sion.sionaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : wick
 * @Date : 2025/7/23 16:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    // 可用的工具
    private final ToolCallback[] availableTools;
    //工具调用管理者
    private final ToolCallingManager toolCallingManager;
    //禁用内置的工具调用机制，自己维护上下文
    private final ChatOptions chatOptions;
    // 保存了工具调用信息的响应
    private ChatResponse toolCallChatResponse;


    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        // 禁用Spring AI 内置的工具调用机制，自己维护上下文
        this.chatOptions = ToolCallingChatOptions.builder()
                .internalToolExecutionEnabled(false)
                .build();
    }


    /**
     * 处理当前状态并决定下一步行动
     *
     * @return boolean
     */
    @Override
    public boolean think() {
        if(getNextStepPrompt()!= null && getNextStepPrompt().isEmpty()){
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }

        List<Message> messageList = getMessageList();
        // chatOptions 的核心作用是：为单次 LLM API 调用提供动态的、覆盖默认设置的配置参数
        // 这里是为了取消Spring AI 托管工具调用
        // 自主控制工具调用的生命周期，思考-行动-观察
        Prompt prompt = new Prompt(messageList,chatOptions);

        try{
            // 获取带工具选项的响应
            ChatResponse chatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();

            // 记录响应，用于Act阶段
            this.toolCallChatResponse = chatResponse;

            // 获取工具调用结果
            assert chatResponse != null;
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

            // 输出提示信息
            String result = assistantMessage.getText();
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            log.info(getName() + " thinks: " + result);
            log.info(getName() + " calls tools num: " + toolCallList.size());

            String toolCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("Tool: %s, Input: %s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));

            log.info(getName() + " tool call info: \n" + toolCallInfo);

            if (toolCallList.isEmpty()){
                // 只有不调用工具时，才记录助手消息
                getMessageList().add(assistantMessage);
                return false;
            }else {
                // 需要调用工具，无需记录助手消息，因为调用工具时会自动记录
                return true;
            }
        }catch (Exception e){
            log.error("ToolCallAgent:{} think failed: {}",getName(),e.getMessage());
            getMessageList().add(new AssistantMessage("ToolCallAgent execute failed: " + e.getMessage()));
            return false;
        }


    }

    @Override
    public String act() {
        if(!toolCallChatResponse.hasToolCalls()){
            return "No tool calls to execute.";
        }
        // 获取工具调用结果

        // 调用工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);

        // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用返回的结果
        setMessageList(toolExecutionResult.conversationHistory());

        // 当前工具调用的结果
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        String results = toolResponseMessage.getResponses().stream()
                .map(toolResponse -> String.format("Tool: %s, Output: %s", toolResponse.name(), toolResponse.responseData()))
                .collect(Collectors.joining("\n"));

        // 判断是否调用了终止工具
        boolean terminateToolCalled = toolResponseMessage.getResponses().stream()
                .anyMatch(toolResponse -> "doTerminate".equals(toolResponse.name()));
        if(terminateToolCalled){
            setState(AgentState.FINISHED);
        }

        log.info(results);
        return results;

    }
}
