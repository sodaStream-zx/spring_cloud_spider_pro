package parsercore.pareser;

import commoncore.customUtils.BeanGainer;
import commoncore.entity.httpEntity.ResponseData;
import commoncore.entity.httpEntity.ResponsePage;
import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.paresEntity.MyNew;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.Istore.IMysqlDao;
import parsercore.dbUtils.gainDao.IGain.IRedisDao;
import parsercore.paresUtil.ParesContent;

/**
 * @author 一杯咖啡
 * @desc 解析器
 * @createTime 2018-12-21-15:49
 */
@Component(value = "parse")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParesProcess implements IParesProcess {

    private static final Logger log = Logger.getLogger(ParesProcess.class.getName());
    private ParesContent paresContent;
    @Autowired
    IRedisDao iRedisDao;
    @Autowired
    IMysqlDao IMysqlDao;
    @Value(value = "${my.responseList.redisKey}")
    private String listKey;
    private DomainRule domainRule = null;

    public ParesProcess() {
        this.paresContent = BeanGainer.getBean("parseContent", ParesContent.class);
    }

    /**
     * desc:解析器
     **/
    @Override
    public void parseRun(ResponseData pageData) {
        log.info("开始解析-----");
        log.debug("解析数据 ：" + pageData.toString());

        if (domainRule != null && pageData.getSiteName().equals(domainRule.getSiteName())) {
            ResponsePage page = new ResponsePage(pageData.getDatum(), pageData.getCode(), pageData.getContentType(), pageData.getContent());
            MyNew myNew = (MyNew) paresContent.paresContent(page, domainRule);
            if (myNew != null) {
                IMysqlDao.insertNew(myNew);
            }
        } else {
            log.info("当前解析规则不可用，从redis 中获取 指定的解析器 ");
            domainRule = iRedisDao.getDomainRuleFromRedis(pageData.getSiteName());
            this.parseRun(pageData);
        }
    }
}
