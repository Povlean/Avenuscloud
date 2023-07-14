package com.ean.yygh.hosp.controller;

import com.ean.yygh.common.result.Result;
import com.ean.yygh.hosp.service.HospitalSetService;
import com.ean.yygh.model.hosp.HospitalSet;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Resource
    private HospitalSetService hospitalSetService;

    @GetMapping("/findAll")
    public Result<List<HospitalSet>> list() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") Integer id) {
        Boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
