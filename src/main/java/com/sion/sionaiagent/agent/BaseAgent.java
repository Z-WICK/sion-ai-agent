package com.sion.sionaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.sion.sionaiagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : wick
 * @Date : 2025/7/23 15:42
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // 核心属性
    private String name;

    //提示
    private String systemPrompt;
    private String nextStepPrompt;

    // 最大步数
    private int maxStep = 20;
    private int currentStep = 0;

    // 状态
    private AgentState state = AgentState.IDLE;

    //LLM
    private ChatClient chatClient;

    // Memory(自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    public String run(String userPrompt) {
        if (this.state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + this.state);

        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty userPrompt");
        }

        // 更改状态
        state = AgentState.RUNNING;

        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));

        // 保存结果列表
        List<String> results = new ArrayList<>();

        try {
            for (int i = 0; i < maxStep && state != AgentState.FINISHED; i++) {
                int stepNum = i + 1;
                currentStep = stepNum;
                log.info("Executing Step {}:", stepNum);

                // 执行当前步骤
                String stepResult = step();
                String result = "Step " + stepNum + ":" + stepResult;
                results.add(result);
            }

            // 检查是否超出步骤限制
            if (currentStep >= maxStep) {
                log.warn("Max step limit reached. Terminating agent.");
                state = AgentState.FINISHED;
                results.add("Max step limit reached. Terminating agent.");
            }
            return String.join("\n", results);
        }catch (Exception e){
            state = AgentState.ERROR;
            log.error("Agent execution failed", e);
            return "Agent execution failed: " + e.getMessage();
        }finally {
            // 清理资源
            this.cleanup();
        }


    }

    /**
     * 执行单个步骤
     *
     * @return {@link String }
     */
    public abstract String step();


    /**
     * 清理资源
     */
    protected void cleanup() {
        // 子类可以重写此方法来清理资源
    }
}
