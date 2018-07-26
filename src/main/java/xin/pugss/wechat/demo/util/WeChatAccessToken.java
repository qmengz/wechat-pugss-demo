package xin.pugss.wechat.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by shuyun on 2018/7/25.
 */
public class WeChatAccessToken {

    public static final Logger logger = LoggerFactory.getLogger(WeChatAccessToken.class);

    private String access_token;
    private int expires_in;
    private Date time = new Date();

    public boolean isExpires() {
        long secends = System.currentTimeMillis() - this.time.getTime();
        return secends > this.expires_in * 1000 ? true : false;
    }

    //类静态对象
    private static WeChatAccessToken accessToken;

    //单例模式实现
    public static String getAccessToken() throws Exception {
        if (accessToken == null) {
            try {
                getWeChatAccessToken();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } else if (accessToken.isExpires()) {
            try {
                getWeChatAccessToken();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return accessToken.getAccess_token();
    }

    @Override
    public String toString() {
        return "WeChatAccessToken [access_token=" + access_token + ", expires_in=" + expires_in + ", time=" + time + ", isExpires()=" + isExpires() + "]";
    }

    private static void getWeChatAccessToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String secret = "8c5f718c862734e70b70e6ca7277c32f";
        String appid = "wxcd98f8458382b162";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        try {
            HttpClient httpclient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");//设置客户端请求字符集！
            logger.info("HTTP_STATUS=" + httpclient.executeMethod(getMethod));
            InputStream is = getMethod.getResponseBodyAsStream();
            String resp = IOUtils.toString(is, "UTF-8");
            JSONObject jo = new JSONObject(resp);
            if (resp.indexOf("access_token")>0){
                String value = mapper.writeValueAsString(jo);
                accessToken = mapper.readValue(value, WeChatAccessToken.class);
            }else {
                throw new Exception(jo.getString("errcode").toString());
            }
        } catch (Exception e){
            logger.error("获取access_token错误");
        }
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

}
