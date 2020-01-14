package xzy.lovelybj.finalassignment.service;

import xzy.lovelybj.finalassignment.bean.Poem;

import java.util.List;
import java.util.Set;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
public interface MainService {
    /**
     * 根据输入的条件以及朝代查询诗
     * @param searchInput
     * @param dynasty
     * @return
     */
    List<Poem> search(String searchInput, String dynasty);

    /**
     * 根据输入的诗人名字联想
     * @param searchInput
     * @param dynasty
     * @return
     */
    List<Poem> reminder(String searchInput, String dynasty);

    /**
     * 同步数据
     * @param url
     */
    void syncData(String url);
}
