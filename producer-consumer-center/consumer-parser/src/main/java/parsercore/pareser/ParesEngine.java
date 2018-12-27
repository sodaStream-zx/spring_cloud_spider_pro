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
 * @desc 解析器
 * @createTime 2018-12-21-15:49
 */
@Component
public class ParesEngine implements IParesEngine {

    private static final Logger log = Logger.getLogger(ParesEngine.class.getName());
    @Autowired
    ParesContent paresContent;
    @Autowired
    IRedisDao iRedisDao;
    @Autowired
    IMysqlDao IMysqlDao;
    @Value(value = "${my.responseList.redisKey}")
    private String listKey;
    private DomainRule domainRule = null;

    @Override
    public void parseRun() {
        //默认解析
    }

    /**
     * desc:解析器
     **/
    @Override
    public void parseRun(ResponseData pageData) {
        log.info("开始解析-----");
        if (domainRule != null && pageData.getSiteName().equals(domainRule.getSiteName())) {
            ResponsePage page = new ResponsePage(pageData.getDatum(), pageData.getCode(), pageData.getContentType(), pageData.getContent());
            MyNew myNew = (MyNew) paresContent.paresContent(page, domainRule);
            if (myNew != null) {
                IMysqlDao.insertNew(myNew);
            }
        } else {
            domainRule = iRedisDao.getDomainRuleFromRedis(pageData.getSiteName());
            this.parseRun(pageData);
        }
    }
}
