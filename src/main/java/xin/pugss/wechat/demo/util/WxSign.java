package xin.pugss.wechat.demo.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by shuyun
 */
public class WxSign {

    public static void main(String[] args) throws Exception {
        String jsapi_ticket="jsapi_ticket";
        String url = "http://cmsplus.com.cn";
        Map<String, String> ret = jsSdkSign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
            //System.out.println(entry.getKey() + "======== " + entry.getValue());
        }
        System.out.println("signature:  "+ret.get("signature") +  ": timestamp " +ret.get("timestamp"));
        System.out.println(createLinkString(ret));
    }

    //对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }


    /***
     * 生成JS-SDK权限验证的签名
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static Map<String, String> jsSdkSign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        //System.out.println(string1);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    /**
     * 卡券扩展字段及签名生成算法
     * @param api_ticket
     * @param card_id
     * @param openid
     * @return
     */
    public static Map<String, String> cardSign(String api_ticket, String card_id,String openid) {
        Map<String, String> ret = new HashMap<String, String>();
        String timestamp = create_timestamp();
        StringBuffer string1 = new StringBuffer();
        String signature = "";
        //排序初始化
        List<String> list = new ArrayList<String>();
        list.add(create_timestamp());
        list.add(card_id);
        list.add(openid);
        list.add(api_ticket);
        Collections.sort(list);
        for(int i = 0; i < list.size(); i++){
            string1.append(list.get(i));
        }
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.toString().getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("card_id", card_id);
        ret.put("openid", openid);
        ret.put("api_ticket", api_ticket);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
