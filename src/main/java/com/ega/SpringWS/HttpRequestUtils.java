/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author sa
 */

//перелік HTTP заголовків, з яких можна отримати IP клієнта
public class HttpRequestUtils {
    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "X-Real-IP",
        "REMOTE_ADDR"
    };

    //повертає IP клієнта, для цього спочатку перевіряємо HTTP заголовки на наявність в них ІР адреси (якщо використовується проксі)
    //якщо в заголовках нічого немає, встановлюємо ІР адресу мережевого вузла, що передав запит.
    public static String getClientIpAddress() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for (String header: IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    //повертає Метод HTTP запита
    public static String getHttpMethod() {


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getMethod();
    }

    //повертає URL шлях запиту
    public static String getPath() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getRequestURI();
    }
    
    //повертає тіло запиту
    public static String getBody()   {

        String body = "";
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        InputStream br = null;
        StringBuilder sb = new StringBuilder();
        
        BufferedReader reader = null;
        
        
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            try {
                    br = request.getInputStream();
//                    br.reset();
                    for (int ch; (ch = br.read()) != -1; ) {
                        sb.append((char) ch);
                    }

                    body = sb.toString();
                } catch (IOException ex) {
                body = "Error getting body: "+ex.getMessage();
            }
        
        }
        
        return body;
    }
    
    
    //повертає заголовки запиту
    public static Map<String,String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    Enumeration<String> headerNames = request.getHeaderNames();

    if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                    System.out.println("Header: "+headerName+": <" + headerValue+">");
                    headers.put(headerName, headerValue);
            }
    }        
        return headers;
    }
    
}
