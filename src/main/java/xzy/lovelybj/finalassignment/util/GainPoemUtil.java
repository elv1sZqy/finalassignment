package xzy.lovelybj.finalassignment.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xzy.lovelybj.finalassignment.bean.Poem;

import java.io.IOException;
import java.util.*;


/**
 * @author zhuQiYun
 * @create 2020/1/2
 * @description :  爬取网站上的一些诗
 */
@Component
public class GainPoemUtil {
    private static Logger log = LoggerFactory.getLogger(GainPoemUtil.class);

    private static Map<String, String> dynastyMap;

    static {
        dynastyMap = new HashMap<>();
        dynastyMap.put("唐代", "唐");
        dynastyMap.put("宋代", "宋");

    }

    public List<Poem> getPoemsFromUrl(String url) {
        List<String> links = getLinks(url);
        if (!CollectionUtils.isEmpty(links)) {
            List<Poem> poems = new ArrayList<>();
            for (String link : links) {
                Poem poem = new Poem();
                SetPoem(link, poem);
                if (Objects.nonNull(poem)) {
                    poems.add(poem);
                }
            }
            log.info("一共同步了{}首诗", poems.size());
            return poems;
        }
        return null;
    }

    /**
     * 获取详细诗的连接
     *
     * @return
     * @throws IOException
     */
    public static List<String> getLinks(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements list = document.getElementsByClass("sons");
        Elements as = list.get(0).getElementsByTag("a");
        List<String> links = new ArrayList<>(as.size());

        for (Element a : as) {
            //各个的连接
            String href = a.attr("abs:href");
            links.add(href);
        }
        return links;
    }

    public static void SetPoem(String url, Poem poem) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements sons = null;
        Element poemContent = null;
        Elements poemRemake = null;
        try {
            sons = document.getElementsByClass("sons");
            poemContent = sons.get(0).getAllElements().get(1);
            poemRemake = sons.get(1).getElementsByClass("contyishang").get(0).getElementsByTag("p");
        } catch (Exception e) {
            log.error("读取网页信息异常,{}", e);
            return;
        }

        String poemName = poemContent.getElementsByTag("h1").text();
        String content = poemContent.getElementsByClass("contson").get(0).text();
        String poetNameAndDynasty = poemContent.getElementsByClass("source").text();
        String poetInfo = null;
        String poetPic = null;
        if (!poetNameAndDynasty.contains("佚名")) {
            Element sonspic = document.getElementsByClass("sonspic").first();
            poetInfo = sonspic.getElementsByTag("p").get(1).text();
            poetPic = sonspic.getElementsByTag("img").first().attr("src");
        }
        String tag = sons.get(0).getElementsByClass("tag").get(0).text();
        for (Element element : poemRemake) {
            String text = element.text();
            String substring = text.length() > 3 ? text.substring(0, 2) : "";
            switch (substring) {
                case "注释":
                    String remark = text.substring(3);
                    poem.setRemake(remark);
                    break;
                case "译文":
                    String translation = text.substring(3);
                    poem.setTranslation(translation);
                    break;
                default:
                    break;
            }
        }
        String[] poetNameAndDynastyArgs = poetNameAndDynasty.split("：");

        if (poetNameAndDynastyArgs != null) {
            String dynasty = dynastyMap.get(poetNameAndDynastyArgs[0]);
            dynasty = dynasty == null ? poetNameAndDynastyArgs[0] : dynasty;
            poem.setDynasty(dynasty);
            poem.setPoetName(poetNameAndDynastyArgs[1]);
        }
        poem.setId(System.currentTimeMillis());
        poem.setContent(content);
        poem.setPoemName(poemName);
        poem.setPoetPic(poetPic);
        poem.setPoetInfo(poetInfo);
        poem.setTag(tag);

    }

}
