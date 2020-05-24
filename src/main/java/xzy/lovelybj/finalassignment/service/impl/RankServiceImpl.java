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
    public List<RankDTO> getWeekRank() {
        String weekLeaderBoardRedisKey = redisUtil.getWeekLeaderBoardRedisKey();
        List<RankDTO> result = getRankInfo(weekLeaderBoardRedisKey);
        return result;
    }
    @Override
    public List<RankDTO> getMonthRand() {
        String monthLeaderBoardRedisKey = redisUtil.getMonthLeaderBoardRedisKey();
        List<RankDTO> result = getRankInfo(monthLeaderBoardRedisKey);
        return result;
    }

    private List<RankDTO> getRankInfo(String weekLeaderBoardRedisKey) {
        Set<String> rang = redisUtil.getRang(weekLeaderBoardRedisKey, 0, 9);
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("id", rang);
        List<Poem> poems = esUtil.search(queryBuilder, rang.size());
        List<RankDTO> result = new ArrayList<>();
        rang.forEach(id -> {
            poems.forEach(poem -> {
                if (id.equals(poem.getId().toString())) {
                    RankDTO rankDTO = new RankDTO();
                    rankDTO.setPoem(poem);
                    setClassType(rankDTO);
                    rankDTO.setPicLinkUrl(CommonUtil.getRandomPicUrl());
                    result.add(rankDTO);
                }
            });
        });
        return result;
    }

    private void setClassType(RankDTO rankDTO) {
        int id = random.nextInt(2);
        id += 1;
        String classType = String.format(CLASS_FRMATER, id);
        rankDTO.setClassType(classType);
    }
}
