package com.byc.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * Created by baiyc
 * 2020/5/19/019 16:58
 * Description：处理消息
 */
@Slf4j
@Service
@EnableScheduling
public class MessageService {
    final Vector<HttpServletResponse> list_res= new Vector();

    public Vector<HttpServletResponse> getListRes() {
        return list_res;
    }

    @Scheduled(initialDelay = 0, fixedDelay = 3*1000)
    public void run() {
        this.removeErrorResponse();
        Iterator<HttpServletResponse> it = list_res.iterator();
        Random rand =new Random();
        int num=rand.nextInt(100);
        while(it.hasNext())
        {
            PrintWriter pw;
            try {
                pw = it.next().getWriter();
                if(pw == null || pw.checkError()) {
                    continue;
                }
                pw.write("data:获取随机数： " + num + "\n\n");
                pw.flush();
            } catch (Exception e) {
                log.error("定时任务异常");
            }
        }
    }

    public synchronized void removeErrorResponse(){
        Iterator<HttpServletResponse> it = list_res.iterator();
        while(it.hasNext()) {
            PrintWriter pw;
            try {
                pw = it.next().getWriter();
                if(pw == null){
                    it.remove();
                    continue;
                } else if(pw.checkError()){
                    pw.close();
                    it.remove();
                    continue;
                }
            } catch (Exception e) {
                it.remove();
            }
        }
    }
}
