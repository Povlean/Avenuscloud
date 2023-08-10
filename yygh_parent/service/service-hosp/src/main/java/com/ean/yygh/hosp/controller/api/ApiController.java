package com.ean.yygh.hosp.controller.api;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ean.yygh.common.exception.YyghException;
import com.ean.yygh.common.helper.HttpRequestHelper;
import com.ean.yygh.common.result.Result;
import com.ean.yygh.common.result.ResultCodeEnum;
import com.ean.yygh.common.utils.MD5;
import com.ean.yygh.hosp.service.HospitalService;
import com.ean.yygh.hosp.service.HospitalSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.queue.PredicatedQueue;
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
    private HospitalSetService hospitalSetService;

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


}
