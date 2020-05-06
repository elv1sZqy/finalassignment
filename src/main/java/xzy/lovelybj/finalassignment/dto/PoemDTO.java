package xzy.lovelybj.finalassignment.dto;

import lombok.Data;
import xzy.lovelybj.finalassignment.bean.Poem;

/**
 * @ClassName PoemDTO
 * @Author Elv1s
 * @Date 2020/5/3 14:42
 * @Description:
 */
@Data
public class PoemDTO extends Poem {
    private Integer clickRate;
    private Long rank;
}
