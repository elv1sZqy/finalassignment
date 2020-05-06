package xzy.lovelybj.finalassignment.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.dto.RecommendDTO;
import xzy.lovelybj.finalassignment.service.RecommendService;
import xzy.lovelybj.finalassignment.util.CommonUtil;
import xzy.lovelybj.finalassignment.util.ESUtil;

import java.util.*;

/**
 * @ClassName RecommendServiceImpl
 * @Author Elv1s
 * @Date 2020/4/12 17:28
 * @Description:
 */
@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private ESUtil esUtil;

    private Random random = new Random();

    @Override
    public List<RecommendDTO> getRecommend() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Integer total = esUtil.getPoemsCount(boolQueryBuilder);
        int startIndex = random.nextInt(total);
        List<Poem> poems = esUtil.search(boolQueryBuilder, 20, startIndex);
        List<RecommendDTO> result = new ArrayList<>();
        List<String> randomPicUrl = CommonUtil.getRandomPicUrl(poems.size());

        for (int i = 0; i < poems.size(); i++) {
            RecommendDTO recommendDTO = new RecommendDTO();
            Poem poem = poems.get(i);
            if (Objects.isNull(poem) || null == poem.getId()) {
                continue;
            }
            String[] tagArr = poem.getTag().split("，");
            String tag = null;
            if (tagArr.length > 2) {
                int index = random.nextInt(2);
                tag = tagArr[index];
                while (tag.equals("唐诗三百首")) {
                    int i1 = index++;
                    if (i1 > tagArr.length) {
                        break;
                    }
                    tag = tagArr[i1];
                }
            } else {
                tag = tagArr[0];
            }
            recommendDTO.setTag(tag);
            recommendDTO.setPoem(poem);
            recommendDTO.setPicLink(randomPicUrl.get(i));
            result.add(recommendDTO);
        }
        return result;
    }
}
