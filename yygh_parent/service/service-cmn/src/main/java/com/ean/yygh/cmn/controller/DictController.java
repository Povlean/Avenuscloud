package com.ean.yygh.cmn.controller;

import com.ean.yygh.cmn.service.DictService;
import com.ean.yygh.common.result.Result;
import com.ean.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description:数据字典接口
 * @author:Povlean
 */
@Api("数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin("http://localhost:9528")
public class DictController {

    @Resource
    private DictService dictService;

    // 导入数据字典
    @PostMapping("/importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }

    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable("id") Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

}
