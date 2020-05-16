package xzy.lovelybj.finalassignment.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.dto.PoemDTO;
import xzy.lovelybj.finalassignment.service.PoemService;
import xzy.lovelybj.finalassignment.util.ESUtil;
import xzy.lovelybj.finalassignment.util.GainPoemUtil;
import xzy.lovelybj.finalassignment.util.MailUtil;
import xzy.lovelybj.finalassignment.util.RedisUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
@Service
public class PoemServiceImpl implements PoemService {
    private Logger logger = LoggerFactory.getLogger(PoemServiceImpl.class);
    @Autowired
    private ESUtil esUtil;
    @Autowired
    private GainPoemUtil gainPoemUtil;
    @Autowired
    private RedisUtil redisUtil;

    private static String template = "一首好的古诗推荐给好学的你，这次推荐的是%s的《%s》，希望你能喜欢☺：%s";

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
        BoolQueryBuilder condition = QueryBuilders.boolQuery();
        condition.should(QueryBuilders.wildcardQuery("poemName", "*" + searchInput + "*"));
        condition.should(QueryBuilders.matchQuery("poemName", searchInput));
        condition.should(QueryBuilders.matchQuery("content", searchInput));
        boolQueryBuilder.must(condition);
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
    public int syncData(String url) {
        List<Poem> poemsFromUrl = gainPoemUtil.getPoemsFromUrl(url);
        esUtil.savePoems(poemsFromUrl);
        int size = poemsFromUrl.size();
        logger.info("一共同步了{}首诗", size);
        return size;
    }

    @Override
    public PoemDTO getInfo(Long id) {
        PoemDTO poemDTO = new PoemDTO();
        Poem poem = esUtil.search(QueryBuilders.termQuery("id", id), 1).get(0);
        BeanUtils.copyProperties(poem, poemDTO);
        Map<String, Object> singleRankAndScore = redisUtil.getSingleRankAndScore(redisUtil.getMonthLeaderBoardRedisKey(), id.toString());
        poemDTO.setRank((Long) singleRankAndScore.get("rank") + 1L);
        poemDTO.setClickRate(((Double) singleRankAndScore.get("score")).intValue());
        return poemDTO;
    }

    @Override
    public List<Poem> searchByPoetName(String poetName, String dynasty, TermQueryBuilder unIncludeQueryBuilder, int size) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // fixme 装好插件以后就把这个去掉
        builder.must(QueryBuilders.termQuery("poetName", poetName));
        builder.must(QueryBuilders.termQuery("dynasty", dynasty));
        builder.mustNot(unIncludeQueryBuilder);
        return esUtil.search(builder, size);
    }

    @Override
    public String getNewPoem(String[] cis) {
        String[] args = new String[]{"python", "C:\\Users\\NewB1\\Desktop\\jinglei-master\\__main__.py", "惊雷", "紫电", "乌云", "多情自古空余恨" };
        if (null != cis) {
            for (int i = 0; i < cis.length; i++) {
                String ci = cis[i];
                if (StringUtils.isNotBlank(ci)) {
                    args[i + 2] = ci;
                }
            }
        }
        String read = null;
        try {
            Process process = Runtime.getRuntime().exec(args);
            InputStream errorStream = process.getErrorStream();
            String error = read(errorStream);
            if (StringUtils.isNotBlank(error)) {
                logger.error("生成歌词失败:{}", error);
            }
            read = read(process.getInputStream());
            logger.info("生成的歌词是:\n{}", read);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return read;
    }

    private String read(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,
                "GBK"));

        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = in.readLine()) != null) {
            sb.append(line).append("\n");
        }

        in.close();
        return sb.toString();
    }

    @Override
    public boolean sendEmail(Long id, String email, String friendName) {
        try {
            PoemDTO info = getInfo(id);
            String content = packagePoemContent(info);
            MailUtil.sendMail(email, "为您推荐一首好的古诗", friendName, content);
        } catch (Exception e) {
            logger.error("邮件发送失败:{}", e);
            return false;
        }
        return true;
    }

    private String packagePoemContent(PoemDTO info) {
        String content = String.format(template, info.getPoetName(), info.getPoemName(), info.getContent());
        if (StringUtils.isNotBlank(info.getTranslation())) {
            content += "<br>可能你会看不懂，细心的朋友为你准备了古诗的翻译：" + info.getTranslation();
        }
        content += "<br>更多优秀的古诗还请您来<a  href='localhost:8080/index' title=\"全球最强古诗搜索平台\"> 全球最强古诗搜索平台</a>看看";
        return content;
    }
}
