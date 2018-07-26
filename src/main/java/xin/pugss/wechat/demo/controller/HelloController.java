package xin.pugss.wechat.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by shuyun on 2018/7/25.
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String helloHtml(HashMap<String, Object> map){
        map.put("hello","欢迎进入HTML页面");
        return "index";
    }

    @RequestMapping("/htt")
    @ResponseBody
    public String htt(HashMap<String, Object> map){
        map.put("aa","cc");
        System.out.println("dsadasdad sa");
        return "dsadasdad";
    }

}
