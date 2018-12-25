package spider.myspider;


import commoncore.entity.SiteConfig;
import commoncore.parseTools.ParesUtil;
import commoncore.parseTools.SerializeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import spider.myspider.redisSpider.RedisManager;
import spider.myspider.spiderComponent.MyRequester;
import spider.spiderCore.spiderConfig.configUtil.ConfigurationUtils;

import java.util.concurrent.TimeUnit;

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
    private RedisManager redisManager;
    // @Autowired
    //private RamDBManager ramDBManager;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 爬虫调度执行组件
     */
    @Autowired
    private ParesUtil paresUtil;
    @Autowired
    private MyRequester myRequester;
    @Autowired
    private SerializeUtil serializeUtil;
    @Autowired
    private MySpider mySpider;


    /**
     * desc: 初始化爬虫,监听消息
     */
    public boolean initToRun() {
        try {
            while (true){
                //读取redis队列任务，并开始抓取；阻塞直到能取出值
                String siteConfigString = "";
                try {
                    while (true) {
                        siteConfigString = (String) redisTemplate.opsForList().leftPop("sites");
                        if ("".equals(siteConfigString) || null == siteConfigString) {
                            LOG.error("redis 网站配置数据为空");
                            pause(1, 0);
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
                    Object scObject = serializeUtil.deserializeToObject(siteConfigString);
                    SiteConfig siteConfig = (SiteConfig) scObject;
                    LOG.info("【" + siteConfig.getSiteName() + "】爬虫装载中------------->>>");
                    /**
                     * DataToDB 数据持久化组件 param(tableName)
                     * paresUtil 网页解析组件 param(siteConfig,dataToDB)
                     * visitor 网页解析器
                     * mySpider 爬虫组合APP
                     * abstractDBmanager 数据库管理组件
                     */
                    ConfigurationUtils.setTo(mySpider, redisManager);
                    mySpider.setAbstractDbManager(redisManager);
                    mySpider.initMySpider(siteConfig, myRequester, paresUtil);
                    mySpider.getConfig().setTopN(1000);
                    LOG.info(this.toString());

                    //10秒自动关闭爬虫
           /* new Thread(() -> {
                pause(10, 0);
                mySpider.stop();
            }, "关闭线程").start();*/

                    mySpider.startFetcher(mySpider);
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

    /**
     * desc:线程休眠
     **/
    public void pause(Integer second, long mills) {
        try {
            LOG.warn("停止任务线程休眠" + second + "秒------------");
            TimeUnit.SECONDS.sleep(second);
            TimeUnit.MILLISECONDS.sleep(mills);
        } catch (InterruptedException e) {
            LOG.error("线程休眠异常");
        }
    }

    @Override
    public String toString() {
        return "\nMySpiderEngine{" +
                "\n  ramDBManager : " + redisManager.getClass().getName() +
                "\n  redisTemplate : " + redisTemplate.getClass().getName() +
                "\n  myRequester : " + myRequester.getClass().getName() +
                '}';
    }
}
