package spider.myspider;


import commoncore.customUtils.SerializeUtil;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.loadEntity.SiteConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.spiderCore.iexecutorCom.ISpider;

import java.util.Optional;

/**
 * 爬虫组件类
 *
 * @author 一杯咖啡
 */
@Component
public class SpiderEngine {
    private static final Logger LOG = Logger.getLogger(SpiderEngine.class.getSimpleName());
    private RedisTemplate redisTemplate;
    private ISpider iSpider;
    private SiteConfig siteConfig;

    @Autowired
    public SpiderEngine(RedisTemplate redisTemplate, ISpider iSpider, SiteConfig siteConfig) {
        this.redisTemplate = redisTemplate;
        this.iSpider = iSpider;
        this.siteConfig = siteConfig;
    }

    /**
     * desc: 初始化爬虫,监听消息
     */
    public boolean initToRun() {
        while (true) {
            //读取redis队列任务，并开始抓取；阻塞直到能取出值
            String siteConfigString;
            while (true) {
                siteConfigString = (String) redisTemplate.opsForList().leftPop("sites");
                if ("".equals(siteConfigString) || null == siteConfigString) {
                    LOG.error("redis 网站配置数据为空");
                    SleepUtil.pause(1, 0);
                } else {
                    break;
                }
            }
            //获取序列化的字符串 生成siteConfig对象 （一个新对象）
            Optional<SiteConfig> optionalSiteConfig = SerializeUtil.deserializeToObject(siteConfigString);
            if (optionalSiteConfig.isPresent()) {
                LOG.info("【" + optionalSiteConfig.get().getSiteName() + "】爬虫装载中------------->>>");
                SiteConfig sc = optionalSiteConfig.get();
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
                afterStop();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * desc:stop
     **/
    public boolean stopSpider() {
        return iSpider.stopSpider();
    }

    public void afterStop() {
        this.initToRun();
    }
}
