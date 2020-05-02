package xzy.lovelybj.finalassignment.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xzy.lovelybj.finalassignment.aop.AddReadTimes;
import xzy.lovelybj.finalassignment.aop.CanQueryFromRedis;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.service.PoemService;

import java.util.List;

/**
 * @author zhuQiYun
 * @create 2019/12/2
 * @description :
 */
@Controller
public class PoemController {

    @Autowired
    private PoemService poemService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @CanQueryFromRedis
    @GetMapping("/search")
    @ResponseBody
    public List<Poem> search(String searchInput, String dynasty) {
        return poemService.search(searchInput, dynasty);
    }

    /**
     * 根据输入的诗人名字联想
     *
     * @param searchInput
     * @param dynasty
     * @return
     */
    @CanQueryFromRedis
    @GetMapping("/reminder")
    @ResponseBody
    public List<Poem> reminder(String searchInput, String dynasty) {
        if (StringUtils.isBlank(searchInput)) {
            return null;
        }
        return poemService.reminder(searchInput, dynasty);
    }

    @AddReadTimes
    @GetMapping("info/{id}")
    @ResponseBody
    public Poem getInfo(@PathVariable Long id){
        return poemService.getInfo(id);
    }

    @PostMapping("/syncData")
    @ResponseBody
    public void syncData(String url) {
        poemService.syncData(url);
    }
}
