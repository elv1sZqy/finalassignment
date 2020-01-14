package xzy.lovelybj.finalassignment.bean;

import lombok.Data;

/**
 * @author zhuQiYun
 * @create 2019/12/30
 * @description :
 */
@Data
public class Poem {
    /**
     * 诗名
     */
    private String poemName;
    /**
     * 内容
     */
    private String content;
    /**
     * 诗人
     */
    private String poetName;
    /**
     * 朝代
     */
    private String dynasty;
    /**
     * 译文
     */
    private String translation;
    /**
     * 注释
     */
    private String remake;
    /**
     * 标签
     */
    private String tag;

}
