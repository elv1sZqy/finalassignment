package xzy.lovelybj.finalassignment.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.service.PoemService;
import xzy.lovelybj.finalassignment.util.ESUtil;
import xzy.lovelybj.finalassignment.util.GainPoemUtil;

import java.util.List;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
@Service
public class PoemServiceImpl implements PoemService {

    @Autowired
    private ESUtil esUtil;
    @Autowired
    private GainPoemUtil gainPoemUtil;

    @Override
    public List<Poem> search(String searchInput, String dynasty) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(dynasty)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("dynasty", dynasty));
        }
        if (StringUtils.isNotBlank(searchInput)) {
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
            boolQueryBuilder2.should(QueryBuilders.matchQuery("content", searchInput));
            // 设置诗人的权重大于诗名
            boolQueryBuilder2.should(QueryBuilders.matchQuery("poetName", searchInput).boost(2f));
            boolQueryBuilder2.should(QueryBuilders.matchQuery("poemName", searchInput));
            boolQueryBuilder.must(boolQueryBuilder2);
        }
        List<Poem> search = esUtil.search(boolQueryBuilder, 4);
        return search;
    }

    @Override
    public List<Poem> reminder(String searchInput, String dynasty) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(dynasty)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("dynasty", dynasty));
        }
        // 设置诗人的权重大于诗名
        boolQueryBuilder.should(QueryBuilders.matchQuery("poetName", searchInput).boost(2f));
        boolQueryBuilder.should(QueryBuilders.matchQuery("poemName", searchInput));
        List<Poem> search = esUtil.search(boolQueryBuilder, 4);
        return search;
    }

    @Override
    public void syncData(String url) {
        List<Poem> poemsFromUrl = gainPoemUtil.getPoemsFromUrl(url);
        esUtil.savePoems(poemsFromUrl);
    }

    @Override
    public Poem getInfo(Long id) {
        return esUtil.search(QueryBuilders.termQuery("id", id), 1).get(0);
    }
}
