package parsercore.parseExecutor;

import commoncore.customUtils.BeanGainer;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.httpEntity.ResponsePage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.mysqlDao.IMysqlDao;
import parsercore.dbUtils.redisDao.IRedisDao;
import parsercore.dbUtils.reposi.MyNewDao;
import parsercore.paresEntity.DomainRule;
import parsercore.paresEntity.MyNew;
import parsercore.paresRuleCore.core.IRuleFactory;
import parsercore.paresUtil.IParseProcess;

/**
 * @author 一杯咖啡
 * @desc 解析器
 * @createTime 2018-12-21-15:49
 */
@Component(value = "paresExecutor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultParesExecutor implements IParesExecutor {

    private static final Logger log = Logger.getLogger(DefaultParesExecutor.class.getName());
    private IParseProcess IParseProcess;
    private IRuleFactory<DomainRule> iRuleFactory;
    @Autowired
    IRedisDao iRedisDao;
    @Autowired
    IMysqlDao IMysqlDao;
    @Autowired
    private MyNewDao myNewDao;
    @Value(value = "${my.responseList.redisKey}")
    private String listKey;

    public DefaultParesExecutor() {
        this.IParseProcess = BeanGainer.getBean("parseContent", IParseProcess.class);
        this.iRuleFactory = BeanGainer.getBean("", IRuleFactory.class);
    }

    /**
     * desc:解析器
     **/
    @Override
    public void parseRun(ResponseData pageData) {
        log.info("开始解析数据 ：" + pageData.pumpInfo());
        //从rulefactory中获取解析器
        DomainRule domainRule = iRuleFactory.getRule(pageData.getSiteName());
        //DomainRule domainRule = null;
        if (domainRule != null && pageData.getSiteName().equals(domainRule.getSiteName())) {
            ResponsePage page = new ResponsePage(pageData.getDatum(), pageData.getCode(), pageData.getContentType(), pageData.getContent());
            MyNew myNew = (MyNew) IParseProcess.paresContent(page, domainRule);
            if (myNew != null) {
                log.info(myNew.getURL());
                myNewDao.save(myNew);
                //IMysqlDao.insertNew(myNew);
            }
            log.info("该url 未解析出新闻内容");
        } else {
            log.error("mysql中无" + pageData.getSiteName() + "解析规则");
            SleepUtil.pause(1, 0);
            log.info("未获取到解析器，放入二次解析列表" + pageData.getSiteName());
        }
    }
}
