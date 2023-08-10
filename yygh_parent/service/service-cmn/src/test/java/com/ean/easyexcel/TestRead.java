package com.ean.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * @description:读取操作测试
 * @author:Povlean
 */
public class TestRead {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\Asphyxia\\Desktop\\userdata.xlsx";
        EasyExcel.read(fileName,UserData.class,new EasyExcelListener())
                .sheet()
                .doRead();
    }
}
