package com.crudcore.services;

import commoncore.Mappers.UrlRuleMapper;
import commoncore.entity.loadEntity.UrlRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Twilight
 * @desc
 * @createTime 2019-01-24-11:31
 */
public class UrlRuleService implements ICrudService<UrlRule> {
    @Autowired
    private UrlRuleMapper urlRuleMapper;

    @Override
    public List<UrlRule> findAll() {
        return urlRuleMapper.findAll();
    }

    @Override
    public UrlRule findById(int tid) {
        return urlRuleMapper.getOne(tid);
    }

    @Override
    public UrlRule save(UrlRule urlRule) {
        return urlRuleMapper.save(urlRule);
    }

    @Override
    public void deleteById(int tid) {
        urlRuleMapper.deleteById(tid);
    }

    @Override
    public UrlRule modify(UrlRule urlRule) {
        return urlRuleMapper.save(urlRule);
    }
}
