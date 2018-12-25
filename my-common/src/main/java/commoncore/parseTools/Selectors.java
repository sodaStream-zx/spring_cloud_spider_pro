package commoncore.parseTools;


import commoncore.entity.responseEntity.ResponsePage;
import org.jsoup.nodes.Document;

/**
 * 网页标签选择器
 */
public class Selectors {

    /**
     * desc: 详细标签选择器
     * @Return:
     **/
    public String detaliSelect(ResponsePage responsePage, String[] selectList) {
        String text;
        Document document = responsePage.doc();
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
     * @Return:
     **/
    public String IdClassSelect(ResponsePage responsePage, String[] selectList) {
        String text;
        Document document = responsePage.doc();
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
