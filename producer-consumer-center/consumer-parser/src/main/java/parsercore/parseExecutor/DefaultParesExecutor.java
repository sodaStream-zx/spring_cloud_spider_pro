package parsercore.parseExecutor;

import commoncore.customUtils.BeanGainer;
import commoncore.customUtils.SleepUtil;
import commoncore.entity.httpEntity.ParseData;
import commoncore.entity.loadEntity.DomainRule;
import commoncore.entity.loadEntity.MyNew;
import commoncore.entity.loadEntity.jpaDao.MyNewDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.mysqlDao.IMysqlDao;
import parsercore.dbUtils.redisDao.IRedisDao;
import parsercore.paresRuleCore.core.IRuleFactory;
import parsercore.paresUtil.IParseProcess;

import java.util.Optional;

/**
 * @author 一杯咖啡
 * @desc 解析器
 * @createTime 2018-12-21-15:49
 */
@Component(value = "paresExecutor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultParesExecutor implements IParesExecutor<ParseData> {

    private static final Logger log = Logger.getLogger(DefaultParesExecutor.class.getName());
    private IParseProcess<MyNew, ParseData, DomainRule> IParseProcess;
    private IRuleFactory<DomainRule> iRuleFactory;
    IRedisDao iRedisDao;
    IMysqlDao iMysqlDao;
    private MyNewDao myNewDao;

    @Autowired
    public DefaultParesExecutor(IRedisDao iRedisDao, IMysqlDao iMysqlDao,
                                MyNewDao myNewDao) {
        this.iRedisDao = iRedisDao;
        this.iMysqlDao = iMysqlDao;
        this.myNewDao = myNewDao;
        this.IParseProcess = BeanGainer.getBean("parseContent", IParseProcess.class);
        this.iRuleFactory = BeanGainer.getBean("", IRuleFactory.class);
    }

    /**
     * desc:解析器
     **/
    @Override
    public void parseRun(ParseData data) {
        log.debug("开始解析数据......");
        //从rulefactory中获取解析器
        DomainRule domainRule = iRuleFactory.getRule(data.getSiteName());
        //DomainRule domainRule = null;
        if (domainRule != null && data.getSiteName().equals(domainRule.getSiteName())) {
            Optional<MyNew> myNew = Optional.ofNullable(IParseProcess.paresContent(data, domainRule));
            if (myNew.isPresent()) {
                log.info(myNew.get().getURL());
                myNewDao.save(myNew.get());
                //IMysqlDao.insertNew(myNew);
                log.info("DONE::" + data.pumpInfo());
            } else {
                log.info("NO CONTENT :: " + data.pumpInfo());
            }
        } else {
            log.error("未能获取到网站:【" + data.getSiteName() + "】解析规则");
            SleepUtil.pause(1, 0);
            log.info("未获取到解析器[" + data.getSiteName() + "]放入二次解析列表");
        }
    }
}
