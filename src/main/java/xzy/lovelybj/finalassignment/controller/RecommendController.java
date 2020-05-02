package xzy.lovelybj.finalassignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xzy.lovelybj.finalassignment.dto.RecommendDTO;
import xzy.lovelybj.finalassignment.service.RecommendService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName RankController
 * @Author Elv1s
 * @Date 2020/4/11 17:54
 * @Description:
 */
@Controller
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping("/recommend")
    public String recommend(Model model) {
        List<RecommendDTO> recommend = recommendService.getRecommend();
        Set<String> tags = new HashSet<>();
        recommend.forEach(recommendDTO -> tags.add(recommendDTO.getTag()));
        model.addAttribute("recommend", recommend);
        model.addAttribute("tags", tags);
        return "/recommend";
    }
}
