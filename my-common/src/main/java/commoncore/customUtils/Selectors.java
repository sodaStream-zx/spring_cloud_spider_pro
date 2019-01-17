package commoncore.customUtils;


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
     * @param doc        页面数据
     * @param selectList 选择器规则
     * @Return: String
     **/
    public static String detaliSelect(Document doc, String[] selectList) {
        String text;
        for (String x : selectList) {
            text = doc.select(x).text().trim();
            if (!text.trim().equals("")) {
                return text;
            }
        }
        return "";
    }

    /**
     * desc: ID CLASS 选择器
     *
     * @param doc        页面数据
     * @param selectList 选择器规则
     * @Return:String
     **/
    public static String IdClassSelect(Document doc, String[] selectList) {
        String text;
        for (String x : selectList) {
            text = doc.select("." + x.trim()).text().trim();
            if (text.equals("")) {
                text = doc.select("#" + x.trim()).text().trim();
                if (text.equals("")) {
                    text = doc.select(x).text().trim();
                }
            }
            return text;
        }
        return "提取失败";
    }
}
