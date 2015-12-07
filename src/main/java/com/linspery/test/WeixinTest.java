package com.linspery.test;

import com.javafx.tools.doclets.formats.html.SourceToHTMLConverter;
import com.linspery.util.WeixinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Linspery on 15/12/1.
 */
public class WeixinTest {
    private static String IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";
    public static void main(String[] args) {
        WeixinUtil weixinUtil = new WeixinUtil();
        String token = weixinUtil.getAccessToken().getAccesstoken();
        System.out.println("票据"+token);
        System.out.println("时间"+weixinUtil.getAccessToken().getExpiresin());
        String url = IP_URL.replace("ACCESS_TOKEN",token);
        for (String e:weixinUtil.getIPAddress(url).getIPlist()) {
            System.out.println("IP:"+e);
        }


        String str = "*杭州";
        str.indexOf(0);
        System.out.println( str.charAt(0));
        if(str.charAt(0)=='*'){
            System.out.println("wa");
            str.substring(1);
            System.out.println(str.substring(1));
        }

        String path = "/Users/Linspery/Documents/javaprojects/weixin/web/image/qrcode.jpg";

        try {
            String mediaId =weixinUtil.upload(path,token,"thumb");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }
}
