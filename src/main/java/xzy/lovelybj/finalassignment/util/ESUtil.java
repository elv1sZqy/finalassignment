package xzy.lovelybj.finalassignment.util;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.frameworkset.elasticsearch.ElasticSearchHelper;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xzy.lovelybj.finalassignment.bean.Poem;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
@Component
public class ESUtil {

    private final String DEFAULT_INDEX_NAME = "poems";

    private ClientInterface restClientUtil = ElasticSearchHelper.getRestClientUtil();

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public List<Poem> search(QueryBuilder queryBuilder, Integer size, Integer from) {
        // poems就是在ES中的索引
        SearchRequest searchRequest = new SearchRequest(DEFAULT_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(size);
        if (Objects.nonNull(from)) {
            searchSourceBuilder.from(from);
        }

        // 高亮关键字
        HighlightBuilder highlighter = new HighlightBuilder();
        String preTag = "<i style=\"color:#de2140\">";
        String postTag = "</i>";
        highlighter.field("poemName").preTags(preTag).postTags(postTag);
        highlighter.field("poetName").preTags(preTag).postTags(postTag);
        highlighter.field("content").numOfFragments(0).preTags(preTag).postTags(postTag);

        searchSourceBuilder.highlighter(highlighter);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (search != null) {
            SearchHit[] hits = search.getHits().getHits();
            List<Poem> collect = Arrays.stream(hits).map(documentFields -> {
                Poem poem = JSON.parseObject(documentFields.getSourceAsString(), Poem.class);
                Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                if (highlightFields != null) {
                    for (Map.Entry<String, HighlightField> highlightFieldEntry : highlightFields.entrySet()) {
                        String field = highlightFieldEntry.getKey();
                        String replaceContent = highlightFieldEntry.getValue().fragments()[0].string();
                        switch (field) {
                            case "poemName":
                                poem.setPoemName(replaceContent);
                                break;
                            case "content":
                                poem.setContent(replaceContent);
                                break;
                            case "poetName":
                                poem.setPoetName(replaceContent);
                                break;
                            default:
                                break;
                        }
                    }
                }
                return poem;
            }).collect(Collectors.toList());

            return collect;
        }
        return Collections.emptyList();
    }

    public List<Poem> search(QueryBuilder queryBuilder, Integer size) {
       return search(queryBuilder, size, null);
    }

    public void savePoems(List<Poem> poemList) {
        restClientUtil.addDocuments(DEFAULT_INDEX_NAME, "doc", poemList);
    }

    /**
     * 查询一共有多少诗
     *
     * @param boolQueryBuilder
     * @return
     */
    public Integer getPoemsCount(BoolQueryBuilder boolQueryBuilder) {
        // poems就是在ES中的索引
        SearchRequest searchRequest = new SearchRequest(DEFAULT_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long totalHits = search.getHits().getTotalHits();
        return Integer.parseInt(totalHits.toString());
    }

}
