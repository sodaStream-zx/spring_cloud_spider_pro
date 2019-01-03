package spider.spiderCore.spiderConfig;

public class DefaultConfigImp extends IConfigImp {
    /**
     * desc:获取默认配置
     * @Return: Configuration
     **/
    public DefaultConfigImp(){
        setConfig(Configuration.getDefault());
    }
}
