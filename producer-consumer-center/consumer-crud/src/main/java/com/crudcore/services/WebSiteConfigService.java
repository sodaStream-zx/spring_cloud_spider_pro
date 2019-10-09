package com.crudcore.services;

import commoncore.Mappers.WebSiteConfMapper;
import commoncore.entity.loadEntity.WebSiteConf;
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
    private WebSiteConfMapper webSiteConfMapper;

    @Override
    public List<WebSiteConf> findAll() {
        return webSiteConfMapper.findAll();
    }

    @Override
    public WebSiteConf findById(int tid) {
        return webSiteConfMapper.getOne(tid);
    }

    @Override
    public WebSiteConf save(WebSiteConf webSiteConf) {
        return webSiteConfMapper.save(webSiteConf);
    }

    @Override
    public void deleteById(int tid) {
        webSiteConfMapper.deleteById(tid);
    }

    @Override
    public WebSiteConf modify(WebSiteConf webSiteConf) {
        return webSiteConfMapper.save(webSiteConf);
    }
}
