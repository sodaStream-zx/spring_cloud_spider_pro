package commoncore.entity.configEntity;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class SiteConfig implements Serializable {
    //网站名字
    private String SiteName;
    //urls
    private String SiteUrl;
    //断点
    private boolean Res;
    //提取页面规则
    private String PageParse;
    //url提取规则
    private String urlPares;
    //任务入口
    private String seeds;
    //抓取深度
    private Integer deepPath;
    //自动抓取
    private boolean autoParse;
    //数据表名称
    private String tableName;

    public SiteConfig() {
    }

    @Override
    public String toString() {
        return "SiteConfig{" +
                "SiteName='" + SiteName + '\'' +
                ", SiteUrl='" + SiteUrl + '\'' +
                ", Res=" + Res +
                ", PageParse='" + PageParse + '\'' +
                ", urlPares='" + urlPares + '\'' +
                ", seeds='" + seeds + '\'' +
                ", deepPath=" + deepPath +
                ", autoParse=" + autoParse +
                ", tableName='" + tableName + '\'' +
                '}';
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getSiteUrl() {
        return SiteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        SiteUrl = siteUrl;
    }

    public boolean isRes() {
        return Res;
    }

    public void setRes(boolean res) {
        Res = res;
    }

    public String getPageParse() {
        return PageParse;
    }

    public void setPageParse(String contentPares) {
        PageParse = contentPares;
    }

    public String getUrlPares() {
        return urlPares;
    }

    public void setUrlPares(String urlPares) {
        this.urlPares = urlPares;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public Integer getDeepPath() {
        return deepPath;
    }

    public void setDeepPath(Integer deepPath) {
        this.deepPath = deepPath;
    }

    public boolean isAutoParse() {
        return autoParse;
    }

    public void setAutoParse(boolean autoParse) {
        this.autoParse = autoParse;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
