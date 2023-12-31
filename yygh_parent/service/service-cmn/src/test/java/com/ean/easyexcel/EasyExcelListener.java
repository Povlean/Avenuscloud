package com.ean.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @description:表格监听项
 * @author:Povlean
 */
public class EasyExcelListener extends AnalysisEventListener<UserData> {

    // 一行一行读取excel内容，从第二行读取
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息:" + headMap);
    }

    // 读取数据后执行代码
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
