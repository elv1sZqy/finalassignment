package xzy.lovelybj.finalassignment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xzy.lovelybj.finalassignment.aop.QueryAdvice;
import xzy.lovelybj.finalassignment.bean.Poem;

/**
 * @author zhuQiYun
 * @create 2020/4/21
 * @description :
 */
@Controller
public class DetailController {
    private Logger log = LoggerFactory.getLogger(DetailController.class);

    @GetMapping("toDetails")
    public String toDetails(Model model){
        Poem poem = new Poem();
        poem.setContent("3任务热无若,认为服务费我,晚饭微服务,服务费网费");
        poem.setPoemName("静夜思");
        poem.setPoetName("李白");
        poem.setDynasty("唐");
        poem.setTranslation("诗的解析-------不错很不错");
        model.addAttribute("poem",poem);
        return "details";
    }
}
