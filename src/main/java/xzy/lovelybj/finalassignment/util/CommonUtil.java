package xzy.lovelybj.finalassignment.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName CommonUtil
 * @Author Elv1s
 * @Date 2020/4/12 18:10
 * @Description:
 */
public class CommonUtil {

    private static Random random = new Random();

    private static String PIC_LINK_FORMATER = "https://i.picsum.photos/id/%d/300/300.jpg";

    public static List<String> getRandomPicUrl(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int id = random.nextInt(1000);
            String url = String.format(PIC_LINK_FORMATER, id);
            list.add(url);
        }
        return list;
    }
}
