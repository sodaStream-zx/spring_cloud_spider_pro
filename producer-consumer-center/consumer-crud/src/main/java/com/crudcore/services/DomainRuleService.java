package com.crudcore.services;

import commoncore.entity.loadEntity.DomainRule;
import commoncore.entity.loadEntity.jpaDao.DomainRuleJpaDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:31
 */
public class DomainRuleService implements ICrudService<DomainRule> {
    @Autowired
    private DomainRuleJpaDao domainRuleJpaDao;

    @Override
    public List<DomainRule> findAll() {
        return domainRuleJpaDao.findAll();
    }

    @Override
    public DomainRule findById(int tid) {
        return domainRuleJpaDao.getOne(tid);
    }

    @Override
    public DomainRule save(DomainRule domainRule) {
        return domainRuleJpaDao.save(domainRule);
    }

    @Override
    public void deleteById(int tid) {
        domainRuleJpaDao.deleteById(tid);
    }

    @Override
    public DomainRule modify(DomainRule domainRule) {
        return domainRuleJpaDao.save(domainRule);
    }
}
