package spider.myspider.executorCompont;

import commoncore.entity.loadEntity.WebSiteConf;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.spiderCore.entitys.FetcherTasks;
import spider.spiderCore.iexecutorCom.INextLinksFilter;


/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-22-16:59
 */
@Component
public class NextLinksFilter implements INextLinksFilter {
    private static final Logger log = Logger.getLogger(NextLinksFilter.class);
    private WebSiteConf webSiteConf;

    @Autowired
    public NextLinksFilter(WebSiteConf webSiteConf) {
        this.webSiteConf = webSiteConf;
    }

    @Override
    public FetcherTasks filter(FetcherTasks tasks) {
        log.debug("过滤后续任务" + tasks.size());
        FetcherTasks filted = new FetcherTasks();
        tasks.getStream()
                .filter(x -> webSiteConf.getDeepPath() >= x.getDeepPath())
                .forEach(filted::addTask);
        log.debug("过滤后续任务后" + filted.size());
        return filted;
    }
}
