package crudcore.services;

import commoncore.entity.loadEntity.WebSiteConf;
import commoncore.entity.loadEntity.jpaDao.WebSiteConfJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:08
 */
@Service
public class WebSiteConfigService implements ICrudService<WebSiteConf> {
    @Autowired
    private WebSiteConfJpaDao webSiteConfJpaDao;

    @Override
    public List<WebSiteConf> findAll() {
        return webSiteConfJpaDao.findAll();
    }

    @Override
    public WebSiteConf findById(int tid) {
        return webSiteConfJpaDao.getOne(tid);
    }

    @Override
    public WebSiteConf save(WebSiteConf webSiteConf) {
        return webSiteConfJpaDao.save(webSiteConf);
    }

    @Override
    public void deleteById(int tid) {
        webSiteConfJpaDao.deleteById(tid);
    }

    @Override
    public WebSiteConf modify(WebSiteConf webSiteConf) {
        return webSiteConfJpaDao.save(webSiteConf);
    }
}
