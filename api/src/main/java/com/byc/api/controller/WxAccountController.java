package com.byc.api.controller;

import com.byc.api.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by baiyc
 * 2020/5/15/015 15:49
 * Description：
 */
@Slf4j
@Controller
@RequestMapping("wx")
public class WxAccountController {

    @Autowired
    MessageService service;

    @RequestMapping("index")
    public String index(){
        return "demo";
    }

    @ResponseBody
    @RequestMapping(value = "message", produces="text/event-stream;charset=UTF-8")
    public DeferredResult<String> messge(HttpServletResponse response){
        try{
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            service.removeErrorResponse();
            service.getListRes().add(response);
            if(!response.getWriter().checkError()){
                response.getWriter().write("data:hello\n\n");
                response.getWriter().flush();
            }
        }catch (Exception e){
            log.error("处理异常",e);
        }
        DeferredResult<String> df = new DeferredResult(60000l);
        return df;
    }
}
