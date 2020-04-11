package xzy.lovelybj.finalassignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName RankController
 * @Author Elv1s
 * @Date 2020/4/11 17:54
 * @Description:
 */
@Controller
public class RecommendController {

    @GetMapping("/recommend")
    public String recommend(){
        System.out.println(".....");
        return "/recommend";
    }
}
