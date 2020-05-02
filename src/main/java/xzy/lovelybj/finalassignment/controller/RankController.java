package xzy.lovelybj.finalassignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.service.RankService;

import java.util.List;

/**
 * @ClassName RankController
 * @Author Elv1s
 * @Date 2020/4/11 17:54
 * @Description:
 */
@Controller
public class RankController {

    @Autowired
    private RankService rankService;

    @GetMapping("/week")
    public String toWeekRankPage(Model model) {
        List<Poem> result = rankService.getWeekRank();
        model.addAttribute("rankList", result);
        return "/rank";
    }
}
