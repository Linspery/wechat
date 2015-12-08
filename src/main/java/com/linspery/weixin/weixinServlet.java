package com.linspery.weixin;

import com.linspery.bean.TextMessage;
import com.linspery.util.CheckUtil;
import com.linspery.util.MessageUtil;
import org.dom4j.DocumentException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

/**
 * Created by Linspery on 15/11/27.
 */
@WebServlet(name = "weixinServlet")
public class weixinServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


         String timestamp =  request.getParameter("timestamp");
         String nonce = request.getParameter("nonce");
         String  echostr = request.getParameter("echostr");
         String signature = request.getParameter("signature");


        PrintWriter out = response.getWriter();

        if (CheckUtil.checkSignature(signature,timestamp,nonce)){
            out.print(echostr);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Map<String,String > map = MessageUtil.xmlToMap(request);

             String toUserName =  map.get("ToUserName");
             String fromUserName = map.get("FromUserName") ;
             String msgType = map.get("MsgType");
             String content = map.get("Content");
            String message = null;
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)){
                if("1".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.firstMenu());
               // }else if ("杭州".equals(content)){
                }else if (content.charAt(0)=='*'){
                    //message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.secondMeun());
                    //message = MessageUtil.initNewsMessage(toUserName,fromUserName);
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.initWeatherMessage(content.substring(1)));
                }else if ("3".equals(content)){
                    message = MessageUtil.initImageMessage(toUserName,fromUserName);
                }else if ("4".equals(content)){
                    message = MessageUtil.initMusicMessage(toUserName,fromUserName);
                }else if ("?".equals(content)||"？".equals(content)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                }else {
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.otherMessage());
                    //System.out.println("test:"+MessageUtil.initWeatherMessage("杭州"));
                }
            }else if (MessageUtil.MESSAGE_EVENT.equals(msgType)){
                String eventype = map.get("Event");
                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventype)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                }else if (MessageUtil.MESSAGE_CLICK.equals(eventype)){
                    message = MessageUtil.initText(toUserName,fromUserName,MessageUtil.menuText());
                }else if (MessageUtil.MESSAGE_VIEW.equals(eventype)){
                    String url = map.get("EventKey");
                    message = MessageUtil.initText(toUserName,fromUserName,url);
                }

            }
            out.print(message);
            System.out.println(message);

        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }


    }
}
