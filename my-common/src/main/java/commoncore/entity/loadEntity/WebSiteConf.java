package commoncore.entity.loadEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Twilight
 * @desc 网站配置，入口，深度等
 * @createTime 2019-01-21-16:20
 */
@Entity
@Table
public class WebSiteConf implements Serializable {
    private static final long serialVersionUID = 9865874136L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int webId;
    @Column
    private String siteName;
    @Column
    private String seeds;
    @Column
    private int deepPath;

    public WebSiteConf() {
    }

    public WebSiteConf(int webId, String siteName, String seeds, int deepPath) {
        this.webId = webId;
        this.siteName = siteName;
        this.seeds = seeds;
        this.deepPath = deepPath;
    }

    @Override
    public String toString() {
        return "WebSiteConf{" +
                "webId=" + webId +
                ", siteName='" + siteName + '\'' +
                ", seeds='" + seeds + '\'' +
                ", deepPath=" + deepPath +
                '}';
    }

    public int getWebId() {
        return webId;
    }

    public void setWebId(int webId) {
        this.webId = webId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSeeds() {
        return seeds;
    }

    public void setSeeds(String seeds) {
        this.seeds = seeds;
    }

    public int getDeepPath() {
        return deepPath;
    }

    public void setDeepPath(int deepPath) {
        this.deepPath = deepPath;
    }
}
