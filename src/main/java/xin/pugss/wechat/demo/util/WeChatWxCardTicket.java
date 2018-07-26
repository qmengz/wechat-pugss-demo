package xin.pugss.wechat.demo.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by shuyun on 2018/7/25.
 */
public class WeChatWxCardTicket {

    public static final Logger logger = LoggerFactory.getLogger(WeChatWxCardTicket.class);
    private String ticket;
    private int expires_in;
    private long timeStamp=System.currentTimeMillis()/1000;

    //单例模式实现
    private static WeChatWxCardTicket WXCARD_TICKET;

    public static String getTicket(){
        if (WXCARD_TICKET==null) {
            try {
                WXCARD_TICKET=getApiTicket();
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }

        if(WXCARD_TICKET.isExpire()){
            try {
                WXCARD_TICKET=getApiTicket();
            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
            return getTicket();
        }else{
            return WXCARD_TICKET.ticket;
        }
    }
    private WeChatWxCardTicket(){
        super();
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isExpire(){
        return System.currentTimeMillis()/1000<(timeStamp+expires_in)?false:true;
    }

    private static WeChatWxCardTicket getApiTicket() throws Exception{

        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token= "+ WeChatAccessToken.getAccessToken() +" &type=wx_card";
        try {
            HttpClient httpclient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");//设置客户端请求字符集！
            logger.info("HTTP_STATUS=" + httpclient.executeMethod(getMethod));
            InputStream is = getMethod.getResponseBodyAsStream();
            String resp = IOUtils.toString(is, "UTF-8");
            JSONObject jo = new JSONObject(resp);
            if ("0".equals(jo.getString("errcode"))){
                WeChatWxCardTicket t=new WeChatWxCardTicket();
                t.setTicket(jo.getString("ticket"));
                t.setExpires_in(jo.getInt("expires_in"));
                return t;
            } else {
                throw new Exception(jo.toString());
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
