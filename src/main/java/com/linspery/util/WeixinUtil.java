package com.linspery.util;


import com.linspery.bean.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Linspery on 15/12/1.
 */
public class WeixinUtil {
    private static String APPID = "wx737d9e3ee8af483d";
    private static String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";
    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static String WEATHER_URL = "http://wthrcdn.etouch.cn/weather_mini?city=LOCAL";
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    public static JSONObject doGetstr(String url){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity!=null){
                String result = EntityUtils.toString(entity,"UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return jsonObject;


    }

    public static JSONObject doPoststr(String url,String outStr){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;

        httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            jsonObject = JSONObject.fromObject(result);
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static AccessToken getAccessToken(){
        AccessToken token = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);

        JSONObject jsonObject = doGetstr(url);
        if (jsonObject!=null){
            token.setAccesstoken(jsonObject.getString("access_token"));
            token.setExpiresin(jsonObject.getInt("expires_in"));
        }
        return token;

    }

    public static IPAddress getIPAddress(String url){
        IPAddress ipAddress = new IPAddress();
        JSONObject jsonObject = doGetstr(url);
        if(jsonObject!=null){
            ipAddress.setIPlist(jsonObject.getJSONArray("ip_list"));
        }
        return ipAddress;

    }

    public static Weather getWeather(String content){
        JSONObject jsonObject = doGetstr(WEATHER_URL.replace("LOCAL",content));
        System.out.println(content);
        Weather weather =new Weather();
        CityWeather cityWeather =new CityWeather();
        Forecast[] forecasts  = new Forecast[5];
       // Forecast forecast = new Forecast();
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        cityWeather.setWendu(jsonObject1.getString("wendu"));
        cityWeather.setGanmao(jsonObject1.getString("ganmao"));
        cityWeather.setCity(jsonObject1.getString("city"));
        JSONArray jsonArray = jsonObject1.getJSONArray("forecast");
        for (int i =0 ;i<jsonArray.size();i++){
            System.out.println(i);
            Forecast forecast = new Forecast();
            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
            forecast.setFengxiang(jsonObject2.getString("fengxiang"));
            forecast.setFengli(jsonObject2.getString("fengli"));
            forecast.setHigh(jsonObject2.getString("high"));
            forecast.setLow(jsonObject2.getString("low"));
            forecast.setType(jsonObject2.getString("type"));
            forecast.setDate(jsonObject2.getString("date"));
            forecasts[i] = forecast;
            System.out.println(forecasts[i].getDate());
        }

        cityWeather.setForecast(forecasts);
        weather.setData(cityWeather);

        return weather;
    }

    //文件上传
    public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("文件不存在");
        }

        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);

        URL urlObj = new URL(url);
        //连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        //设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = sb.toString().getBytes("utf-8");

        //获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        //输出表头
        out.write(head);

        //文件正文部分
        //把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        //结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        JSONObject jsonObj = JSONObject.fromObject(result);
        System.out.println(jsonObj);
        String typeName = "media_id";
        if(!"WEB-INF/image".equals(type)){
            typeName = type + "_media_id";
        }
        String mediaId = jsonObj.getString(typeName);
        return mediaId;
    }



}
