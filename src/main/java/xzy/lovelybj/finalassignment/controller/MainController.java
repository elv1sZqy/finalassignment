package xzy.lovelybj.finalassignment.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.service.MainService;

import java.util.List;
import java.util.Set;

/**
 * @author zhuQiYun
 * @create 2019/12/2
 * @description :
 */
@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Poem> search(String searchInput, String dynasty) {
        return mainService.search(searchInput, dynasty);
    }

    /**
     * 根据输入的诗人名字联想
     *
     * @param searchInput
     * @param dynasty
     * @return
     */
    @GetMapping("/reminder")
    @ResponseBody
    public List<Poem> reminder(String searchInput, String dynasty) {
        if (StringUtils.isBlank(searchInput)) {
            return null;
        }
        return mainService.reminder(searchInput, dynasty);
    }

    @PostMapping("/syncData")
    @ResponseBody
    public void syncData(String url) {
        mainService.syncData(url);
    }
}
