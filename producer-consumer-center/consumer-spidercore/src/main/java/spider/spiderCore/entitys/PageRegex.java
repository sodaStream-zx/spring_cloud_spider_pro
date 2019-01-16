package spider.spiderCore.entitys;

import commoncore.entity.httpEntity.ResponsePage;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Twilight
 * @desc 响应数据正则选择
 * @createTime 2019-01-08-16:13
 */
public class PageRegex {
    private ResponsePage responsePage;

    /**
     * 判断当前Page的URL是否和输入正则匹配
     *
     * @param urlRegex
     * @return
     */
    public boolean matchUrl(String urlRegex) {
        return Pattern.matches(urlRegex, responsePage.getCrawlDatum().getUrl());
    }

    /**
     * 判断当前Page的Http响应头的Content-Type是否符合正则
     *
     * @param contentTypeRegex
     * @return
     */
    public boolean matchContentType(String contentTypeRegex) {
        if (contentTypeRegex == null) {
            return responsePage.getContentType() == null;
        }
        return Pattern.matches(contentTypeRegex, responsePage.getContentType());
    }

    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过attrs("img[src]","abs:src")可获取网页中所有图片的链接
     *
     * @param cssSelector
     * @param attrName
     * @return
     */
    public ArrayList<String> attrs(String cssSelector, String attrName) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(cssSelector);
        for (Element ele : eles) {
            if (ele.hasAttr(attrName)) {
                result.add(ele.attr(attrName));
            }
        }
        return result;
    }

    /**
     * 获取网页中满足指定css选择器的所有元素的指定属性的集合
     * 例如通过attr("img[src]","abs:src")可获取网页中第一个图片的链接
     *
     * @param cssSelector
     * @param attrName
     * @return
     */
    public String attr(String cssSelector, String attrName) {
        return select(cssSelector).attr(attrName);
    }


    public Links links(boolean parseImg) {
        Links links = new Links().addFromElement(responsePage.getDoc(), parseImg);
        return links;
    }

    public Links links() {
        return links(false);
    }


    /**
     * 获取满足选择器的元素中的链接 选择器cssSelector必须定位到具体的超链接 例如我们想抽取id为content的div中的所有超链接，这里
     * 就要将cssSelector定义为div[id=content] a
     *
     * @param cssSelector
     * @return
     */
    public Links links(String cssSelector, boolean parseSrc) {
        Links links = new Links().addBySelector(responsePage.getDoc(), cssSelector, parseSrc);
        return links;
    }

    public Links links(String cssSelector) {
        return links(cssSelector, false);
    }


    public Links regexLinks(RegexRule regexRule, boolean parseSrc) {
        return new Links().addByRegex(responsePage.getDoc(), regexRule, parseSrc);
    }

    public Links regexLinks(RegexRule regexRule) {
        return regexLinks(regexRule, false);
    }

    public ArrayList<String> selectTextList(String cssSelector) {
        ArrayList<String> result = new ArrayList<String>();
        Elements eles = select(cssSelector);
        for (Element ele : eles) {
            result.add(ele.text());
        }
        return result;
    }

    public String selectText(String cssSelector, int index) {
        return ListUtils.getByIndex(selectTextList(cssSelector), index);
    }

    public String selectText(String cssSelector) {
        return select(cssSelector).first().text();
    }

    public ArrayList<Integer> selectIntList(String cssSelector) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (String text : selectTextList(cssSelector)) {
            result.add(Integer.valueOf(text.trim()));
        }
        return result;
    }

    public int selectInt(String cssSelector, int index) {
        String text = selectText(cssSelector, index).trim();
        return Integer.parseInt(text);
    }

    public int selectInt(String cssSelector) {
        return selectInt(cssSelector, 0);
    }

    public ArrayList<Double> selectDoubleList(String cssSelector) {
        ArrayList<Double> result = new ArrayList<Double>();
        for (String text : selectTextList(cssSelector)) {
            result.add(Double.valueOf(text.trim()));
        }
        return result;
    }

    public double selectDouble(String cssSelector, int index) {
        String text = selectText(cssSelector, index).trim();
        return Double.valueOf(text);
    }

    public double selectDouble(String cssSelector) {
        return selectDouble(cssSelector, 0);
    }

    public ArrayList<Long> selectLongList(String cssSelector) {
        ArrayList<Long> result = new ArrayList<Long>();
        for (String text : selectTextList(cssSelector)) {
            result.add(Long.valueOf(text.trim()));
        }
        return result;
    }

    public long selectLong(String cssSelector, int index) {
        String text = selectText(cssSelector, index).trim();
        Long ltext = Long.parseLong(text);
        return ltext;
    }

    public long selectLong(String cssSelector) {
        return selectLong(cssSelector, 0);
    }


    public Elements select(String cssSelector) {
        return responsePage.getDoc().select(cssSelector);
    }

    public Element select(String cssSelector, int index) {
        Elements eles = select(cssSelector);
        int realIndex = index;
        if (index < 0) {
            realIndex = eles.size() + index;
        }
        return eles.get(realIndex);
    }

    public String regex(String regex, int group, String defaultResult) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responsePage.getHtml());
        if (matcher.find()) {
            return matcher.group(group);
        } else {
            return defaultResult;
        }
    }

    public String regex(String regex, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responsePage.getHtml());
        matcher.find();
        return matcher.group(group);
    }

    public String regexAndFormat(String regex, String format) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(responsePage.getHtml());
        matcher.find();
        String[] strs = new String[matcher.groupCount()];
        for (int i = 0; i < matcher.groupCount(); i++) {
            strs[i] = matcher.group(i + 1);
        }
        return String.format(format, strs);
    }

    public String regex(String regex, String defaultResult) {
        return regex(regex, 0, defaultResult);
    }

    public String regex(String regex) {
        return regex(regex, 0);
    }
}
