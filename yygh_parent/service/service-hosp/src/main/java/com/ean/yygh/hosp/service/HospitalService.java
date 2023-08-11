package com.ean.yygh.hosp.service;

import com.ean.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {

    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

}
