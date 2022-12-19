package com.ecnu.esorder;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.ecnu.esorder.dao.EsDAO;
import com.ecnu.esorder.pojo.Order;
import com.ecnu.esorder.service.WriteDataService;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class EsCRUD {

    @Autowired
    WriteDataService tool;

    @Autowired
    EsDAO dao;

    @Test
    public void addItem(){
        Date d1 = new Date();
        tool.handle("E:/BigData/xjd/xjd/xjd_order_filterAll_01.txt");
        Date d2 = new Date();
        System.out.println("time cost is " + (d2.getTime()-d1.getTime())/1000.0 + "s");
    }



    @Test
    public void getItem(){
        try {
            dao.getRowkeyByOrderID(124906164830L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Test
    public void updatePart(){
        dao.updateFlagById("cancel_flag",true, "c204+13233641771_p+9223370439455838807+124906164830");
    }

    @SneakyThrows
    @Test
    public void searchTest(){
        dao.searchByName("京东", "13233641771_p");
    }

    @SneakyThrows
    @Test
    public void searchTest2(){
        dao.searchByFlag("13233641771_p","delete_flag", false);
    }
}
