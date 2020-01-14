package xzy.lovelybj.finalassignment.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.service.MainService;
import xzy.lovelybj.finalassignment.util.ESUtil;
import xzy.lovelybj.finalassignment.util.GainPoemUtil;

import java.util.List;
import java.util.Set;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
@Service
public class MainServiceImpl implements MainService {

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
            boolQueryBuilder2.should(QueryBuilders.matchQuery("poemName", searchInput));
            boolQueryBuilder2.should(QueryBuilders.wildcardQuery("poetName", "*" + searchInput + "*"));
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
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("poetName", "*" + searchInput + "*"));
        List<Poem> search = esUtil.search(boolQueryBuilder, 4);
        return search;
    }

    @Override
    public void syncData(String url) {
        List<Poem> poemsFromUrl = gainPoemUtil.getPoemsFromUrl(url);
        esUtil.savePoems(poemsFromUrl);
    }
}
