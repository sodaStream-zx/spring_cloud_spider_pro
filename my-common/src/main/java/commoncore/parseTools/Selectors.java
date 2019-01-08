package commoncore.parseTools;


import commoncore.entity.httpEntity.ResponsePage;
import org.jsoup.nodes.Document;

/**
 * 网页标签选择器
 *
 * @author Twilight
 */
public class Selectors {

    /**
     * desc: 详细标签选择器
     *
     * @param responsePage 页面数据
     * @param selectList   选择器规则
     * @Return: String
     **/
    public static String detaliSelect(ResponsePage responsePage, String[] selectList) {
        String text;
        Document document = responsePage.getDoc();
        for (String x : selectList) {
            text = document.select(x).text().trim();
            if (!text.trim().equals("")) {
                return text;
            }
        }
        return "";
    }

    /**
     * desc: ID CLASS 选择器
     *
     * @param responsePage 页面数据
     * @param selectList   选择器规则
     * @Return:String
     **/
    public static String IdClassSelect(ResponsePage responsePage, String[] selectList) {
        String text;
        Document document = responsePage.getDoc();
        for (String x : selectList) {
            text = document.select("." + x.trim()).text().trim();
            if (text.equals("")) {
                text = document.select("#" + x.trim()).text().trim();
                if (text.equals("")) {
                    text = document.select(x).text().trim();
                }
            }
            return text;
        }
        return "提取失败";
    }
}
