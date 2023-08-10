package com.ean.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:写数据测试
 * @author:Povlean
 */
public class TestWrite {
    public static void main(String[] args) {
        // 编写文件路径和文件名
        String fileName = "C:\\Users\\Asphyxia\\Desktop\\userdata.xlsx";
        // 构建list
        List<UserData> userDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setUid(i);
            userData.setUsername("ean" + i);
            userDataList.add(userData);
        }
        // 使用Api写入用户信息
        EasyExcel.write(fileName, UserData.class)
                .sheet("用户信息")
                .doWrite(userDataList);
    }
}
