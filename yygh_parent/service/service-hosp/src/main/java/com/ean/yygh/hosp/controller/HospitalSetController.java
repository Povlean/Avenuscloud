package com.ean.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ean.yygh.common.result.Result;
import com.ean.yygh.common.util.MD5;
import com.ean.yygh.hosp.service.HospitalSetService;
import com.ean.yygh.model.hosp.HospitalSet;
import com.ean.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * @description:医院管理系统
 * @author:Povlean
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@CrossOrigin("http://localhost:9528")
public class HospitalSetController {

    @Resource
    private HospitalSetService hospitalSetService;

    // 查询所有医院设置
    @GetMapping("/findAll")
    public Result<List<HospitalSet>> list() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    // 根据id删除医院设置
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") Integer id) {
        Boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 条件查询带分页
    @PostMapping("/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable("current") Integer current,
                                  @PathVariable("limit") Integer limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 构造分页
        Page<HospitalSet> page = new Page<>(current, limit);
        // 构造分页条件
        // WHERE条件语句
        LambdaQueryWrapper<HospitalSet> queryWrapper = new LambdaQueryWrapper();
        String hoscode = hospitalSetQueryVo.getHoscode();
        String hosname = hospitalSetQueryVo.getHosname();
        if (StringUtils.isNotBlank(hoscode) || StringUtils.isNotBlank(hosname)) {
            queryWrapper.eq(HospitalSet::getHoscode, hoscode);
            queryWrapper.like(HospitalSet::getHosname, hosname);
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, queryWrapper);
        return Result.ok(hospitalSetPage);
    }

    // 添加医院设置
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态 1 开启 0 关闭
        hospitalSet.setStatus(1);
        // 签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        // 调用save方法
        boolean flag = hospitalSetService.save(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 根据id获取医院设置
    @GetMapping("/getHospitalSet/{id}")
    public Result getHospitalSetById(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    // 修改医院设置
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 批量删除医院设置
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> ids) {
        boolean flag = hospitalSetService.removeBatchByIds(ids);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 医院设置锁定和解锁
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status) {
        // 获取要修改的医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置医院状态
        hospitalSet.setStatus(status);
        // 更新医院设置信息
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String hoscode = hospitalSet.getHoscode();
        String signKey = hospitalSet.getSignKey();
        //TODO 发送短信
        return Result.ok();
    }
}
