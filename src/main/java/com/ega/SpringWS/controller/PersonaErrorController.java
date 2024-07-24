/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.SpringWS.controller;

import com.ega.SpringWS.HttpRequestUtils;
import com.ega.SpringWS.models.Answer;
import com.ega.SpringWS.models.LogRecord;
import com.ega.SpringWS.services.LogRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;



/**
 * Це контролер, який викликається в разі будь-яких непередбачених помилок
 * має дві реалізаціі, якщо запит йде з веб-браузера, то викликається обробник, який формує html відповідь
 * інакше, якщо запит йде з іншого типу клієнта, то викликається обробник, який формує json відповідь
 */
@Controller
public class PersonaErrorController implements ErrorController{

    /**
     * Error Attributes in the Application
     */
    private final ErrorAttributes errorAttributes;
    private final LogRecordService logService = new LogRecordService();
    private final static String ERROR_PATH = "/error";

    /**
     * Controller for the Error Controller
     * @param errorAttributes
     */
    public PersonaErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
   
    /**
     * Supports the HTML Error View
     * @param request
     * @return
     */
 
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions
            .defaults()
            .including(ErrorAttributeOptions.Include.STACK_TRACE);
        return new ModelAndView("/errors/error", getHttpErrorAttributes(request, options));
    }

    /**
     * Supports other formats like JSON, XML
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public Answer error(HttpServletRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions
            .defaults()
            .including(ErrorAttributeOptions.Include.STACK_TRACE);
        Map<String, Object> body = getErrorAttributes(request, options);
        HttpStatus status = getStatus(request);
        Answer ans = Answer.builder()
                .status(Boolean.FALSE)
                .descr(status.toString())
                .result(body.toString())
                .build();
        
        writeLog(ans);
        
        return ans;
    }

    /**
     * Returns the path of the error page.
     *
     * @return повертаємо шлях до помилки
     */
    public String getErrorPath() {
        return ERROR_PATH;
    }

    //отримуємо атрибути помилки, якщо помилку віддаємо як JSON об'єкт
    private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                   ErrorAttributeOptions options) {

        WebRequest wr = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(wr, options);
    
    }
    
    //отримуємо атрибути помилки, якщо помилку віддаємо як HTML сторінку
    private Map<String, Object> getHttpErrorAttributes(HttpServletRequest request,
                                                   ErrorAttributeOptions options) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);

        return this.getErrorAttributes((HttpServletRequest) requestAttributes, options);
    }
    
    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("jakarta.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            }
            catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    //окрема функція для запису лога помилок
    private void writeLog(Answer ans){
    
        LogRecord log = new LogRecord();
        
        log.setIp(HttpRequestUtils.getClientIpAddress());
        log.setHttpMethod(HttpRequestUtils.getHttpMethod());
        log.setError(!ans.getStatus());
        log.setResource(HttpRequestUtils.getPath());
        log.setResult(ans);
        log.setDescr(ans.getDescr());
        log.setBody("");
        
        logService.addRecord(log);
        
    }

}
