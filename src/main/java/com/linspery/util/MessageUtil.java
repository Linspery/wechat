package com.linspery.util;


import com.linspery.bean.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Linspery on 15/11/28.
 */
public class MessageUtil {
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_SCANCODE = "scancode_push";


    public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String,String> map = new HashMap<String,String>();
        SAXReader reader = new SAXReader();
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for (Element e:list) {
            map.put(e.getName(),e.getText());

        }
        ins.close();
        return map;
    }

    public static String textMessageToXml(TextMessage textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml",textMessage.getClass());
        return  xstream.toXML(textMessage);

    }
    public static String imageMessageToXml(ImageMessage imageMessage){
        XStream xstream = new XStream();
        xstream.alias("xml",imageMessage.getClass());
        return  xstream.toXML(imageMessage);

    }

    public static String musicMessageToXml(MusicMessage musicMessage){
        XStream xstream = new XStream();
        xstream.alias("xml",musicMessage.getClass());
        return  xstream.toXML(musicMessage);

    }

    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注,请根据菜单提示进行操作:\n\n");
        sb.append("1.当前时间\n");
        sb.append("2.输入 *+城市 获取天气信息 如:*北京\n\n");
        sb.append("回复?调出次菜单");
        return sb.toString();

    }

    public static String initText(String toUserName,String fromUserName ,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(String.valueOf(new Date().getTime()));
        text.setContent(content);
        return textMessageToXml(text);
    }

    public static String firstMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("现在的时间是:");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日hh点mm分ss秒");
        sb.append(sdf.format(new Date()));
        return sb.toString();
    }

    public static String secondMeun(){
        StringBuffer sb = new StringBuffer();
        sb.append("今天天气不错哦!!!!");
        return sb.toString();
    }

    public static String otherMessage(){
        StringBuffer sb = new StringBuffer();
        sb.append("你输入的命令不存在!!\n\n");
        sb.append("请输入?重新查看菜单!!");
        return sb.toString();
    }

    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xstream = new XStream();
        xstream.alias("xml",newsMessage.getClass());
        xstream.alias("item",new News().getClass());
        return  xstream.toXML(newsMessage);

    }

    public static String initNewsMessage(String toUserName,String fromUserName){
        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        News news = new News();
        news.setTitle("新闻");
        news.setDescription("描述");
        news.setPicUrl("http://wechat-linspery.coding.io/image/qrcode.jpg");
        news.setUrl("http://www.qq.com");

        newsList.add(news);

        newsMessage.setFromUserName(toUserName);
        newsMessage.setToUserName(fromUserName);
        newsMessage.setCreateTime(String.valueOf(new Date().getTime()));
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticleCount(newsList.size());
        newsMessage.setArticles(newsList);

        return newsMessageToXml(newsMessage);

    }

    public static String initWeatherMessage(String content){
        WeixinUtil weixinUtil =new WeixinUtil();

        Weather weather = weixinUtil.getWeather(content);
        //System.out.println("zhe");
        StringBuffer sb = new StringBuffer();
        //System.out.println("zhe1");
        sb.append(weather.getData().getCity()+"现在温度为:"+weather.getData().getWendu()+"摄氏度"+weather.getData().getGanmao()+"\n");
        sb.append("近5天的温度如:\n");
        for (int i=0;i<weather.getData().getForecast().length;i++) {
            sb.append(weather.getData().getForecast()[i].getDate()+"\n天气为:"+weather.getData().getForecast()[i].getType()+"\n最高温度为:"+weather.getData().getForecast()[i].getHigh()+"\n最低温度为:"+weather.getData().getForecast()[i].getLow()+"\n风力为:"+weather.getData().getForecast()[i].getFengli()+"\n风向为:"+weather.getData().getForecast()[i].getFengxiang()+"\n\n");
        }
        return sb.toString();

    }

    public static String initImageMessage(String toUserName,String fromUserName){
        String message = null;
        Image image =new Image();
        image.setMediaId("WoFiVrFvDLcOErHyHrBfmVkNL8w0hW8FPdCPR1HQ9lpaJ2OWEG8pA5x8WPtqUEko");
        ImageMessage imageMessage =new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setCreateTime(String.valueOf(new Date().getTime()));
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setImage(image);

        message = imageMessageToXml(imageMessage);
        return  message;
    }

    public static String initMusicMessage(String toUserName,String fromUserName){
        String message = null;
        Music music =new Music();
        music.setThumbMediaId("TYw9guMdsrhkjbfnhFfgnhdlS01qTqU9EAQ9eS8DTGF3a4WdUDAMZYzX_RRLIb1-");
        music.setTitle("甜甜的");
        music.setDescription("周杰伦的歌");
        music.setMusicUrl("http://wechat-linspery.coding.io/music/sweat.mp3");
        music.setHQMusicUrl("http://wechat-linspery.coding.io/music/sweat.mp3");
        MusicMessage musicMessage =new MusicMessage();

        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setCreateTime(String.valueOf(new Date().getTime()));
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setMusic(music);

        message = musicMessageToXml(musicMessage);
        return  message;
    }
}


