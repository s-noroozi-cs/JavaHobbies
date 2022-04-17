package com.hobi.proxyaspect.util;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class LogUtil {
    public static void log(HttpServletRequest request,String source){
        String msg = String.format("source:%s, time:%s: The request method: %s, url: %s"
                ,source
                , LocalDateTime.now()
                ,request.getMethod()
                ,request.getRequestURI());
        System.out.println("aspect impl. --> " + msg);
    }
}
