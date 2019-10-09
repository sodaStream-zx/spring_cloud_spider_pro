package com.crudcore.services;

import commoncore.Mappers.DomainRuleMapper;
import commoncore.entity.loadEntity.DomainRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:31
 */
public class DomainRuleService implements ICrudService<DomainRule> {
    @Autowired
    private DomainRuleMapper domainRuleMapper;

    @Override
    public List<DomainRule> findAll() {
        return domainRuleMapper.findAll();
    }

    @Override
    public DomainRule findById(int tid) {
        return domainRuleMapper.getOne(tid);
    }

    @Override
    public DomainRule save(DomainRule domainRule) {
        return domainRuleMapper.save(domainRule);
    }

    @Override
    public void deleteById(int tid) {
        domainRuleMapper.deleteById(tid);
    }

    @Override
    public DomainRule modify(DomainRule domainRule) {
        return domainRuleMapper.save(domainRule);
    }
}
