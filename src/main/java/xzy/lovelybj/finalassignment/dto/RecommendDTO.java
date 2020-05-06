package xzy.lovelybj.finalassignment.dto;

import lombok.Data;
import xzy.lovelybj.finalassignment.bean.Poem;

/**
 * @ClassName RecommendDTO
 * @Author Elv1s
 * @Date 2020/4/12 17:46
 * @Description:
 */
@Data
public class RecommendDTO {
    private String id;
    private String tag;
    private Poem poem;
    private String picLink;
    private TagInfo tagInfo;

    @Data
    public static class TagInfo{
        private String tagName;
        private String tagNum;
    }
}
