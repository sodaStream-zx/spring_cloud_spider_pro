package parsercore.contentSelector.ISelector;

import org.jsoup.nodes.Document;

import java.util.List;

/**
 * desc: 网页内容提取接口 待完善
 **/
public interface ContentSelect {
    String titleSelect(Document doc);

    String urlSelect(Document doc);

    String contSelect(Document doc);

    String mediaSelect(Document doc);

    String authorSelect(Document doc);

    String timeSelect(Document doc);

    List<String> imgSelect(Document doc);

    List<String> srcSelect(Document doc);
}
