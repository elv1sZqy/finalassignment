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
        if (StringUtils.isNotBlank(searchInput)) {
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
            boolQueryBuilder2.should(QueryBuilders.matchQuery("content", searchInput));
            boolQueryBuilder2.should(QueryBuilders.matchQuery("poemName", searchInput));
            boolQueryBuilder2.should(QueryBuilders.wildcardQuery("poetName", "*" + searchInput + "*"));
            boolQueryBuilder.must(boolQueryBuilder2);
        }
        if (StringUtils.isNotBlank(dynasty)) {
            if ("诗人".equals(dynasty)) {
                boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.wildcardQuery("poetName", "*" + searchInput + "*"));
            } else {
                boolQueryBuilder.must(QueryBuilders.termQuery("dynasty", dynasty));
            }
        }
        List<Poem> search = esUtil.search(boolQueryBuilder, 4);
        return search;
    }

    @Override
    public List<Poem> reminder(String searchInput, String dynasty) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("poemName", "*" + searchInput + "*"));
        if (StringUtils.isNotBlank(dynasty)) {
            if ("诗人".equals(dynasty)) {
                boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.wildcardQuery("poetName", "*" + searchInput + "*"));
            } else {
                boolQueryBuilder.must(QueryBuilders.termQuery("dynasty", dynasty));
            }
        }
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
