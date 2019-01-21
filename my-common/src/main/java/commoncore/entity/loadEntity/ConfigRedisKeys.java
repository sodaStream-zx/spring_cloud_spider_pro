package commoncore.entity.loadEntity;

import org.springframework.stereotype.Component;

/**
 * @author Twilight
 * @desc 保存redis 中的配置信息数据表名
 * @createTime 2019-01-21-16:50
 */
@Component
public class ConfigRedisKeys {
    private int keyId;
    //网站配置表名
    private String wsConfHash = "webSiteCnf";
    //网站提取规则表名
    private String urlRuleHash = "urlRuleCnf";
    //网站正文提取规则
    private String domainRuleHash = "domRuleCnf";

    public ConfigRedisKeys() {
    }

    @Override
    public String toString() {
        return "ConfigRedisKeys{" +
                "keyId=" + keyId +
                ", wsConfHash='" + wsConfHash + '\'' +
                ", urlRuleHash='" + urlRuleHash + '\'' +
                ", domainRuleHash='" + domainRuleHash + '\'' +
                '}';
    }

    public ConfigRedisKeys(String wsConfHash, String urlRuleHash, String domainRuleHash) {
        this.wsConfHash = wsConfHash;
        this.urlRuleHash = urlRuleHash;
        this.domainRuleHash = domainRuleHash;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public String getWsConfHash() {
        return wsConfHash;
    }

    public void setWsConfHash(String wsConfHash) {
        this.wsConfHash = wsConfHash;
    }

    public String getUrlRuleHash() {
        return urlRuleHash;
    }

    public void setUrlRuleHash(String urlRuleHash) {
        this.urlRuleHash = urlRuleHash;
    }

    public String getDomainRuleHash() {
        return domainRuleHash;
    }

    public void setDomainRuleHash(String domainRuleHash) {
        this.domainRuleHash = domainRuleHash;
    }
}
