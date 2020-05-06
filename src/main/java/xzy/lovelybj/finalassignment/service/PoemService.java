package xzy.lovelybj.finalassignment.service;

import org.elasticsearch.index.query.TermQueryBuilder;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.dto.PoemDTO;

import java.util.List;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
public interface PoemService {
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
    int syncData(String url);

    /**
     * 根据id获取诗的详情
     * @param id
     * @return
     */
    PoemDTO getInfo(Long id);

    List<Poem> searchByPoetName(String poetName, String dynasty, TermQueryBuilder unIncludeQueryBuilder, int size);

    String getNewPoem(String[] agrs);
}
