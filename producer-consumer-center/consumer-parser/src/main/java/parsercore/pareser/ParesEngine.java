package parsercore.pareser;

import commoncore.entity.paresEntity.DomainRule;
import commoncore.entity.paresEntity.MyNew;
import commoncore.entity.responseEntity.ResponseData;
import commoncore.entity.responseEntity.ResponsePage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parsercore.dbUtils.Istore.IMysqlDao;
import parsercore.dbUtils.gainDao.IGain.IRedisDao;
import parsercore.paresUtil.ParesContent;

/**
 * @author 一杯咖啡
 * @desc 解析器集成
 * @createTime 2018-12-21-15:49
 */
@Component
public class ParesEngine implements IParesEngine {

    private static final Logger log = Logger.getLogger(ParesEngine.class.getName());
    @Autowired
    ParesContent paresContent;
    @Autowired
    IRedisDao iRedisDaoResponseData;
    @Autowired
    IMysqlDao IMysqlDao;
    @Value(value = "${redis.responseList}")
    private String listKey;
    private DomainRule domainRule = null;

    @Override
    public void paresRun() {
        //多线程调度中心 waiting writing
        while (true) {
            log.info("start pareser");
            ResponseData pageData = iRedisDaoResponseData.getResponseDataFromRedis(listKey);
            if (domainRule != null && pageData.getSiteName().equals(domainRule.getSiteName())) {
                ResponsePage page = new ResponsePage(pageData.getDatum(), pageData.getCode(), pageData.getContentType(), pageData.getContent());
                MyNew myNew = (MyNew) paresContent.paresContent(page, domainRule);
                if (myNew !=null) {
                    IMysqlDao.insertNew(myNew);
                }
            } else {
                domainRule = iRedisDaoResponseData.getDomainRuleFromRedis(pageData.getSiteName());
            }
        }
    }
}
