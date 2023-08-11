package com.ean.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ean.yygh.cmn.config.DictListener;
import com.ean.yygh.cmn.mapper.DictMapper;
import com.ean.yygh.cmn.service.DictService;
import com.ean.yygh.model.cmn.Dict;
import com.ean.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        dictList.stream().forEach(dict -> {
            boolean isChildren = this.isChildren(dict.getId());
            dict.setHasChildren(isChildren);
        });
        return dictList;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        // 将服务器的响应值返回给浏览器
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        // 在数据库中查询dict表中的数据
        List<Dict> dictList = this.list();
        // 存放VO类型的list
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        // 将VOlist写到Excel表格中
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(dictEeVoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            // 根据value查询
            QueryWrapper<Dict> queryWrapper = new QueryWrapper();
            queryWrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(queryWrapper);
            return dict.getName();
        } else {
            // 根据dictCode查询
            Dict dict = this.getDictByDictCode(dictCode);
            Long parentId = dict.getId();
            // 根据parent_id和value查询结果
            Dict targetDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return targetDict.getName();
        }
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        return baseMapper.selectOne(queryWrapper);
    }

    private boolean isChildren(Long dictId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id",dictId);
        Long count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
