package xzy.lovelybj.finalassignment.service.impl;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xzy.lovelybj.finalassignment.bean.Poem;
import xzy.lovelybj.finalassignment.dto.RankDTO;
import xzy.lovelybj.finalassignment.service.RankService;
import xzy.lovelybj.finalassignment.util.CommonUtil;
import xzy.lovelybj.finalassignment.util.ESUtil;
import xzy.lovelybj.finalassignment.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author zhuQiYun
 * @create 2020-04-12
 * @description :
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ESUtil esUtil;

    private static Random random = new Random();

    private static String CLASS_FRMATER = "slide slide--%d js-slide";
    @Override
    public List<Poem> getWeekRank() {
        String weekLeaderBoardRedisKey = redisUtil.getWeekLeaderBoardRedisKey();
        Set<String> rang = redisUtil.getRang(weekLeaderBoardRedisKey, 0, 10);
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("id", rang);
        List<Poem> poems = esUtil.search(queryBuilder, rang.size());
        int size = poems.size();
        List<RankDTO> result = new ArrayList<>();
        getClassName(poems,result,size);
        List<String> randomPicUrl = CommonUtil.getRandomPicUrl(size);
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setPicLinkUrl(randomPicUrl.get(i));
        }
        return poems;
    }


    private void getClassName(List<Poem> poems, List<RankDTO> result, int size) {
        for (int i = 0; i < size; i++) {
            RankDTO rankDTO = new RankDTO();
            rankDTO.setPoem(poems.get(i));
            int id = random.nextInt(2);
            id += 1;
            String classType = String.format(CLASS_FRMATER, id);
            rankDTO.setClassType(classType);
            result.add(rankDTO);
        }
    }


}
