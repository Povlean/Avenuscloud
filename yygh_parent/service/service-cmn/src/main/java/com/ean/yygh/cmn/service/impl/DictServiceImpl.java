package com.ean.yygh.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ean.yygh.cmn.mapper.DictMapper;
import com.ean.yygh.cmn.service.DictService;
import com.ean.yygh.model.cmn.Dict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    private boolean isChildren(Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",dictId);
        Long count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
