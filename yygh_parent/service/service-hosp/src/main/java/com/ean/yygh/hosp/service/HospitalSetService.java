package com.ean.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ean.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);

}
