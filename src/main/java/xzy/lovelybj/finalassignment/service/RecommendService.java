package xzy.lovelybj.finalassignment.service;

import xzy.lovelybj.finalassignment.dto.RecommendDTO;

import java.util.List;

/**
 * @ClassName RecommendService
 * @Author Elv1s
 * @Date 2020/4/12 17:27
 * @Description:
 */
public interface RecommendService {
    /**
     * 随机获取推荐诗
     * @return
     */
    List<RecommendDTO> getRecommend();

}
