package com.crudcore.services;

import commoncore.entity.loadEntity.UrlRule;
import commoncore.entity.loadEntity.jpaDao.UrlRuleJpaDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:31
 */
public class UrlRuleService implements ICrudService<UrlRule> {
    @Autowired
    private UrlRuleJpaDao urlRuleJpaDao;

    @Override
    public List<UrlRule> findAll() {
        return urlRuleJpaDao.findAll();
    }

    @Override
    public UrlRule findById(int tid) {
        return urlRuleJpaDao.getOne(tid);
    }

    @Override
    public UrlRule save(UrlRule urlRule) {
        return urlRuleJpaDao.save(urlRule);
    }

    @Override
    public void deleteById(int tid) {
        urlRuleJpaDao.deleteById(tid);
    }

    @Override
    public UrlRule modify(UrlRule urlRule) {
        return urlRuleJpaDao.save(urlRule);
    }
}
