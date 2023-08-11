package com.ean.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ean.yygh.hosp.repository.HospitalRepository;
import com.ean.yygh.hosp.service.HospitalService;
import com.ean.yygh.model.hosp.Hospital;
import com.ean.yygh.vo.hosp.HospitalQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    @Resource
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        log.info(JSONObject.toJSONString(paramMap));
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Hospital.class);
        Hospital targetHospital = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        // 存在则修改，不存在则添加
        if (null != targetHospital) {
            hospital.setStatus(targetHospital.getStatus());
            hospital.setCreateTime(targetHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 0为第一页
        Pageable pageable = PageRequest.of(page, limit, sort);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> hospitalList = hospitalRepository.findAll(example, pageable);
        return hospitalList;
    }
}
