/*
package loadercore.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Component
@Entity
@Table(name = "siteConfig")
public class SiteConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scId;
    //网站名字
    @Column
    private String SiteName;
    //urls
    @Column
    private String SiteUrl;
    //断点
    @Column
    private boolean Res;
    //提取页面规则
    @Column
    private String PageParse;
    //url提取规则
    @Column
    private String urlPares;
    //任务入口
    @Column
    private String seeds;
    //抓取深度
    @Column
    private Integer deepPath;
    //自动抓取
    @Column
    private boolean autoParse;
    //数据表名称
    @Column
    private String tableName;

    public SiteConfig() {
    }

    @Override
    public String toString() {
        return "SiteConfig{" +
                "scId=" + scId +
                ", SiteName='" + SiteName + '\'' +
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

    public int getScId() {
        return scId;
    }

    public void setScId(int scId) {
        this.scId = scId;
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
*/
