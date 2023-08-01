package com.ean.yygh.cmn.controller;

import com.ean.yygh.cmn.service.DictService;
import com.ean.yygh.common.result.Result;
import com.ean.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@Api("数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin("http://localhost:9528")
public class DictController {

    @Resource
    private DictService dictService;

    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

}
