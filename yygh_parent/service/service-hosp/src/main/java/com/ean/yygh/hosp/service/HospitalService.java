package com.ean.yygh.hosp.service;

import com.ean.yygh.model.hosp.Hospital;
import com.ean.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

}
