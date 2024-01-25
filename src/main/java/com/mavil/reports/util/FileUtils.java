package com.mavil.reports.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class FileUtils {

    private static final Log log = LogFactory.getLog(FileUtils.class);

    public static String GET_CONTENT_DISPOSITION(String fileExt) {
        String type = "attachment";
        if (fileExt.contains("data:image") || fileExt.contains("data:application/pdf")) {
            type = "inline";
        }
        return type;
    }

    public static MediaType getMediaType(String fileExt) {
        int beginSubstr = 0;
        if (fileExt.startsWith("data:")) {
            beginSubstr = 5;
        }
        String contentTypePart = fileExt.substring(beginSubstr);
        return MediaType.parseMediaType(contentTypePart);
    }

    public static Map<String, String> getHeaders(String fileName, String fileExt) {
        String fileNameTrim = fileName.trim();
        String auxContentDisp = GET_CONTENT_DISPOSITION(fileExt);
        String contentDisposition = String.format("%s; filename=\"%s\"", auxContentDisp, fileNameTrim);
        return Map.of("Content-disposition", contentDisposition,
                "Pragma", "no-cache");
    }


}
