package com.ean.yygh.hosp.controller.api;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ean.yygh.common.exception.YyghException;
import com.ean.yygh.common.helper.HttpRequestHelper;
import com.ean.yygh.common.result.Result;
import com.ean.yygh.common.result.ResultCodeEnum;
import com.ean.yygh.common.utils.MD5;
import com.ean.yygh.hosp.service.DepartmentService;
import com.ean.yygh.hosp.service.HospitalService;
import com.ean.yygh.hosp.service.HospitalSetService;
import com.ean.yygh.hosp.service.ScheduleService;
import com.ean.yygh.model.hosp.Department;
import com.ean.yygh.model.hosp.Hospital;
import com.ean.yygh.model.hosp.Schedule;
import com.ean.yygh.vo.hosp.DepartmentQueryVo;
import com.ean.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api("医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Resource
    private HospitalService hospitalService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private HospitalSetService hospitalSetService;
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value = "删除科室")
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = paramMap.get("hoscode").toString();
        String hosScheduleId = paramMap.get("hosScheduleId").toString();
        // 签名校验
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MD5SignKey = MD5.encrypt(signKey);
        if(MD5SignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");

        int page = StringUtils.isEmpty(paramMap.get("page").toString()) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit").toString()) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        // 签名校验
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MD5SignKey = MD5.encrypt(signKey);
        if(MD5SignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page , limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }


    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = paramMap.get("hoscode").toString();
        // 参数校验
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MD5SignKey = MD5.encrypt(signKey);
        if (!paramMap.get("sign").toString().equals(MD5SignKey)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "获取分页列表")
    @PostMapping("/department/list")
    public Result<Page<Department>> department(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 非必填
        // String depcode = paramMap.get("depcode").toString();
        // 必须参数校验
        String hoscode = paramMap.get("hoscode").toString();
        int page = StringUtils.isEmpty(paramMap.get("page").toString()) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit").toString()) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MDsignKey = MD5.encrypt(signKey);
        if(!MDsignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        // departmentQueryVo.setDepcode(depcode);
        Page<Department> departments = departmentService.findPageDepartment(page, limit, departmentQueryVo);
        return Result.ok(departments);
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 必须参数校验
        String hoscode = (String)paramMap.get("hoscode");
        //必填
        String depcode = (String)paramMap.get("depcode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MDsignKey = MD5.encrypt(signKey);
        if(!MDsignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation(value = "上传医院")
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 必须参数校验
        String hoscode = paramMap.get("hoscode").toString();
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MDsignKey = MD5.encrypt(signKey);
        if (!MDsignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String logoData = paramMap.get("logoData").toString();
        // 如果不为空，那么需要转换图片格式
        if (!StringUtils.isEmpty(logoData)) {
            String logo = logoData.replace(" ", "+");
            paramMap.put("logoData", logo);
        }
        hospitalService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院信息")
    @PostMapping("/hospital/show")
    public Result<Hospital> hospital(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = paramMap.get("hoscode").toString();
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MDsignKey = MD5.encrypt(signKey);
        if (!MDsignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @ApiOperation(value = "上传科室")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 校验医院签名
        verifySign(paramMap);
        departmentService.save(paramMap);
        return Result.ok();
    }

    // 用于校验
    public void verifySign(Map<String, Object> paramMap) {
        String hoscode = paramMap.get("hoscode").toString();
        if (StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String MDsignKey = MD5.encrypt(signKey);
        if (!MDsignKey.equals(paramMap.get("sign").toString())) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
}
