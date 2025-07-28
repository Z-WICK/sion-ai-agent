package com.sion.sionaiagent.agent;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

/**
 * @Author : wick
 * @Date : 2025/7/23 16:05
 * <p>
 * ReAct (Reasoning and Acting) 模式的代理抽象类
 * 实现了思考-行动的循环模式
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent {


    /**
     * 处理当前状态并决定下一步行动
     *
     * @return 是否需要执行行动，true表示需要执行，false表示不需要执行
     */
    public abstract boolean think();


    /**
     * 执行决定的行动
     *
     * @return 行动执行结果
     */
    public abstract String act();


    /**
     * 执行单个步骤：思考和行动
     *
     * @return 步骤执行结果
     */
    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                return "No action needed";
            }
            return act();
        } catch (Exception e) {
            // 记录异常记录
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }
}