package com.ean.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ean.yygh.hosp.mapper.HospitalSetMapper;
import com.ean.yygh.hosp.service.HospitalSetService;
import com.ean.yygh.model.hosp.HospitalSet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:TODO
 * @author:Povlean
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService{

    @Override
    public String getSignKey(String hoscode) {
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HospitalSet::getHoscode, hoscode);
        HospitalSet hospitalSet = this.getOne(queryWrapper);
        return hospitalSet.getSignKey();
    }

}
