package com.parsercore.contentSelector;

import com.parsercore.contentSelector.ISelector.ContentSelect;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * desc: 网页内容提取实现
 **/
public class ContentSelectUtil implements ContentSelect {
    @Override
    public String titleSelect(Document doc) {
        return null;
    }

    @Override
    public String urlSelect(Document doc) {
        return null;
    }

    @Override
    public String contSelect(Document doc) {
        return null;
    }

    @Override
    public String mediaSelect(Document doc) {
        return null;
    }

    @Override
    public String authorSelect(Document doc) {
        return null;
    }

    @Override
    public String timeSelect(Document doc) {
        return null;
    }

    @Override
    public List<String> imgSelect(Document doc) {
        List<String> listUrl = new ArrayList<>();
        Elements imgs = doc.select("img[src]");
        imgs.stream().filter(x -> x.hasAttr("src"))
                .map(a -> a.attr("abs:src"))
                .forEach(listUrl::add);
        return listUrl;
    }

    @Override
    public List<String> srcSelect(Document doc) {
        List<String> listSrc = new ArrayList<>();
        List<String> imgs = doc.select("*[src]").eachAttr("abs:src");
        imgs.stream().filter(x -> true).forEach(listSrc::add);
        return listSrc;
    }
}
