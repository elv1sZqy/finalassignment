package xzy.lovelybj.finalassignment.dto;

import lombok.Data;
import xzy.lovelybj.finalassignment.bean.Poem;

/**
 * @ClassName RankDTO
 * @Author Elv1s
 * @Date 2020/4/12 15:39
 * @Description:
 */
@Data
public class RankDTO {
    private String classType;
    private Poem poem;
    private String picLinkUrl;
}
