package com.sion.sionaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @Author : wick
 * @Date : 2025/7/27 15:32
 */
public class TerminateTool {

    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate() {
        return "任务结束";
    }
}

