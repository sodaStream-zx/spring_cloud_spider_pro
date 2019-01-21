package commoncore.entity.loadEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Twilight
 * @desc spider 请求和url提取规则
 * @createTime 2019-01-21-16:13
 */
@Entity
@Table
public class UrlRule implements Serializable {
    private static final long serialVersionUID = -125998875;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int urId;
    @Column
    private String siteName;
    //提取页面规则
    @Column
    private String pageParse;
    //url提取规则
    @Column
    private String urlPares;

    @Override
    public String toString() {
        return "UrlRule{" +
                "urId=" + urId +
                ", siteName='" + siteName + '\'' +
                ", pageParse='" + pageParse + '\'' +
                ", urlPares='" + urlPares + '\'' +
                '}';
    }

    public UrlRule() {
    }

    public UrlRule(String siteName, String pageParse, String urlPares) {
        this.siteName = siteName;
        this.pageParse = pageParse;
        this.urlPares = urlPares;
    }

    public int getUrId() {
        return urId;
    }

    public void setUrId(int urId) {
        this.urId = urId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getPageParse() {
        return pageParse;
    }

    public void setPageParse(String pageParse) {
        this.pageParse = pageParse;
    }

    public String getUrlPares() {
        return urlPares;
    }

    public void setUrlPares(String urlPares) {
        this.urlPares = urlPares;
    }
}
