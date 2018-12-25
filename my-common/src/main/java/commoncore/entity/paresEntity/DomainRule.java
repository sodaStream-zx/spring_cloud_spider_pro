package commoncore.entity.paresEntity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 一杯咖啡
 * @desc 解析器配置信息
 * @createTime 2018-11-30-12:40
 */
@Entity
@Table(name = "domainRule")
public class DomainRule implements Serializable {
    private static final long serialVersionUID= 2L;
    @Id
    @GeneratedValue
    private Integer domainId;
    @Column
    private String siteName;
    @Column
    private String title_rule;
    @Column
    private String content_rule;
    @Column
    private String time_rule;
    @Column
    private String media_rule;
    @Column
    private String anthor_rule;

    public DomainRule() {
    }

    public DomainRule(String siteName, String title_rule, String content_rule, String time_rule, String media_rule, String anthor_rule) {
        this.siteName = siteName;
        this.title_rule = title_rule;
        this.content_rule = content_rule;
        this.time_rule = time_rule;
        this.media_rule = media_rule;
        this.anthor_rule = anthor_rule;
    }

    @Override
    public String toString() {
        return "DomainRule{" +
                "domainId=" + domainId +
                ", siteName='" + siteName + '\'' +
                ", title_rule='" + title_rule + '\'' +
                ", content_rule='" + content_rule + '\'' +
                ", time_rule='" + time_rule + '\'' +
                ", media_rule='" + media_rule + '\'' +
                ", anthor_rule='" + anthor_rule + '\'' +
                '}';
    }

    public Integer getDomainId() {
        return domainId;
    }

    public void setDomainId(Integer domainId) {
        this.domainId = domainId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTitle_rule() {
        return title_rule;
    }

    public void setTitle_rule(String title_rule) {
        this.title_rule = title_rule;
    }

    public String getContent_rule() {
        return content_rule;
    }

    public void setContent_rule(String content_rule) {
        this.content_rule = content_rule;
    }

    public String getTime_rule() {
        return time_rule;
    }

    public void setTime_rule(String time_rule) {
        this.time_rule = time_rule;
    }

    public String getMedia_rule() {
        return media_rule;
    }

    public void setMedia_rule(String media_rule) {
        this.media_rule = media_rule;
    }

    public String getAnthor_rule() {
        return anthor_rule;
    }

    public void setAnthor_rule(String anthor_rule) {
        this.anthor_rule = anthor_rule;
    }
}
