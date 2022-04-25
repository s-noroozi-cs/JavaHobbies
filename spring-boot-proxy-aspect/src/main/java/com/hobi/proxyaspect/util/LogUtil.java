package com.hobi.proxyaspect.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void log(HttpServletRequest request,String source){
        String msg = String.format("source:%s, time:%s: The request method: %s, url: %s"
                ,source
                , LocalDateTime.now()
                ,request.getMethod()
                ,request.getRequestURI());
        logger.info(msg);
    }
}
