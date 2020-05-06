package xzy.lovelybj.finalassignment.service;

import xzy.lovelybj.finalassignment.dto.RankDTO;

import java.util.List;

/**
 * @author zhuQiYun
 * @create 2020-04-12
 * @description :
 */
public interface RankService {

    List<RankDTO> getWeekRank();

    List<RankDTO> getMonthRand();
}
