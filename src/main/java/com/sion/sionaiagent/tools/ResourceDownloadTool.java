package com.sion.sionaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.sion.sionaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @Author : wick
 * @Date : 2025/7/13 10:55
 */
public class ResourceDownloadTool {

    public String downloadResource(
            @ToolParam(description = "URL of the resource to download") String url,
            @ToolParam(description = "Name of the file to save the download resource ")String fileName)
    {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;

        try{
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 使用Hutool 的downloadFile 方法下载资源
            HttpUtil.downloadFile(url, FileUtil.file(filePath));
            return "Resource downloaded successfully to" + filePath;

        }catch (Exception e){
            return "Error downloading resource: " + e.getMessage();
        }
    }
}
