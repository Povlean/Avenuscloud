package com.ean.yygh.hosp.controller;

import com.ean.yygh.common.result.Result;
import com.ean.yygh.hosp.service.HospitalService;
import com.ean.yygh.model.hosp.Hospital;
import com.ean.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 */
@Api(tags = "医院管理接口")
@CrossOrigin("http://localhost:9528")
@RequestMapping("/admin/hosp/hospital")
@RestController
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

}
