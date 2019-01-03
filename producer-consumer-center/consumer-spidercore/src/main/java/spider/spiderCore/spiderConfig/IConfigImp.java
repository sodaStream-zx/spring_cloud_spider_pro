package spider.spiderCore.spiderConfig;

import spider.spiderCore.spiderConfig.IConfig.IConfig;

public class IConfigImp implements IConfig {
    protected Configuration configuration;
    @Override
    public Configuration getConfig() {
        return configuration;
    }
    @Override
    public void setConfig(Configuration configuration) {
        this.configuration = configuration;
    }
}
