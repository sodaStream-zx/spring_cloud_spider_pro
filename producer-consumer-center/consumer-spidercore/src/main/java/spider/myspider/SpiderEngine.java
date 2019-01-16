package spider.myspider;


import commoncore.customUtils.SerializeUtil;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.configEntity.SiteConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.iexecutorCom.ISpider;

/**
 * 爬虫组件类
 *
 * @author 一杯咖啡
 */
@Component
public class SpiderEngine {

    private static final Logger LOG = Logger.getLogger(SpiderEngine.class.getSimpleName());

    /**
     * 数据存储组件
     */
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ISpider iSpider;

    @Autowired
    private SiteConfig siteConfig;

    /**
     * desc: 初始化爬虫,监听消息
     */
    public boolean initToRun() {
        try {
            while (true) {
                //读取redis队列任务，并开始抓取；阻塞直到能取出值
                String siteConfigString;
                try {
                    while (true) {
                        siteConfigString = (String) redisTemplate.opsForList().leftPop("sites");
                        if ("".equals(siteConfigString) || null == siteConfigString) {
                            LOG.error("redis 网站配置数据为空");
                            SleepUtil.pause(1, 0);
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    LOG.error("读取redis队列失败" + e.getCause());
                    return false;
                }
                try {
                    //获取序列化的字符串 生成siteConfig对象
                    SiteConfig sc = (SiteConfig) SerializeUtil.deserializeToObject(siteConfigString);
                    LOG.info("【" + sc.getSiteName() + "】爬虫装载中------------->>>");

                    this.siteConfig.setAutoParse(sc.isAutoParse());
                    this.siteConfig.setDeepPath(sc.getDeepPath());
                    this.siteConfig.setPageParse(sc.getPageParse());
                    this.siteConfig.setRes(sc.isRes());
                    this.siteConfig.setSiteName(sc.getSiteName());
                    this.siteConfig.setTableName(sc.getTableName());
                    this.siteConfig.setSiteUrl(sc.getSiteUrl());
                    this.siteConfig.setSeeds(sc.getSeeds());
                    this.siteConfig.setUrlPares(sc.getUrlPares());

                    iSpider.setConfig(siteConfig);
                    iSpider.spiderProcess();
                    LOG.info(this.toString());

                    //10秒自动关闭爬虫
           /* new Thread(() -> {
                pause(10, 0);
                mySpider.stop();
            }, "关闭线程").start();*/
                    return true;
                } catch (Exception e) {
                    LOG.error("初始化爬虫异常: " + e.getCause() + ";messages:" + e.getMessage());
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
