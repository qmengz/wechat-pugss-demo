package xin.pugss.wechat.demo.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.pugss.wechat.demo.util.WeChatJSSDKTicket;
import xin.pugss.wechat.demo.util.WeChatWxCardTicket;
import xin.pugss.wechat.demo.util.WxSign;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by shuyun on 2018/7/25.
 */

@Controller
@RequestMapping("/card")
public class WeChatCardController {

    public static final Logger logger = LoggerFactory.getLogger(WeChatCardController.class);

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String init(@RequestParam(value = "code") String code, Model model) {
        System.out.println("code = " + code);
        String secret = "8c5f718c862734e70b70e6ca7277c32f";
        String appid = "wxcd98f8458382b162";
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
            System.out.println("url:" + url);
            HttpClient httpclient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");//设置客户端请求字符集！
            System.out.println("HTTP_STATUS=" + httpclient.executeMethod(getMethod));
            InputStream is = getMethod.getResponseBodyAsStream();
            String resp = IOUtils.toString(is, "UTF-8");
            System.out.println("resp:  " + resp);
            JSONObject jo = new JSONObject(resp);
            logger.info(jo.toString());
            if (jo.has("openid")) {
                String openid = jo.getString("openid");
                model.addAttribute("openid", openid);
                return "index";
            }
        } catch (Exception e) {
            logger.error("获取access_token出错：",e);
        }
        return null;
    }

    /* 获取jssdk签名
     *
     * @param url
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getSignature", method = {RequestMethod.GET})
    public Object getSignature(@RequestParam(value = "url") String url) {
        try {
            WxSign wxSign = new WxSign();
            logger.info("getSignature url: " + url);
            String ticket = WeChatJSSDKTicket.getTicket();
            Map<String, String> ret = wxSign.jsSdkSign(ticket, url);
            logger.info("ticket：" + ticket);
            return ret;
        } catch (Exception e) {
            logger.error("获取jssdk签名出错：" , e);
        }
        return "";
    }

    /**
     * 获取微信卡券sdk签名
     *
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getWxCardSignature", method = {RequestMethod.GET})
    public Object getWxCardSignature(@RequestParam(value = "openid") String openid) {
        String card_id = "";
        try {
            WxSign wxSign = new WxSign();
            Map<String, String> ret = wxSign.cardSign(WeChatWxCardTicket.getTicket(), card_id, openid);
            return ret;
        } catch (Exception e) {
            logger.error("获取微信卡券sdk签名出错:",e);
        }
        return "";
    }

}
