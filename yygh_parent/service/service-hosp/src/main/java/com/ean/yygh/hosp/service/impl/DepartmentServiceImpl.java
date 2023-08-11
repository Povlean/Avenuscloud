package com.ean.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ean.yygh.hosp.repository.DepartmentRepository;
import com.ean.yygh.hosp.service.DepartmentService;
import com.ean.yygh.model.hosp.Department;
import com.ean.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository departmentRepository;

    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        // 0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit);
        // 创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        // 创建匹配器，查找如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() // 构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // 改变默认字符
                .withIgnoreCase(true); // 改变默认大小写忽略方式：忽略大小写
        // 创建实例
        Example<Department> example = Example.of(department, matcher);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public void save(Map<String, Object> paramMap) {
        String mapStr = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(mapStr, Department.class);
        Department targetDep = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (null != targetDep) {
            // 数据库中有该科室，那么将更新信息保存到数据库
            BeanUtils.copyProperties(department, targetDep, Department.class);
            departmentRepository.save(targetDep);
        } else {
            // 当数据库没有科室，则将输入信息保存到数据库中
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            departmentRepository.save(department);
        }
    }

}
