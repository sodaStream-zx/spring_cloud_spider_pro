package spider.spiderCore.spiderConfig.configUtil;


import spider.spiderCore.spiderConfig.IConfig.IConfig;

public class ConfigurationUtils {
//    public static void addParent(Object child, Configured parent){
//        if(child instanceof Configured){
//            Configured configuredChild = (Configured) child;
//            configuredChild.setParent(parent);
//        }
//    }

    public static void setTo(IConfig from, Object... targets) {
        for(Object target:targets){
            if (target instanceof IConfig) {
                IConfig configuredTarget = (IConfig) target;
                configuredTarget.setConfig(from.getConfig());
            }
        }
    }

}
