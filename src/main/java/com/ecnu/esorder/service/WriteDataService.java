package com.ecnu.esorder.service;

import com.ecnu.esorder.dao.EsDAO;
import com.ecnu.esorder.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class WriteDataService {
    int curLine = 1;
    int checkPointLine = 0;

    @Autowired
    EsDAO service;
    public void handle(String path){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line = br.readLine(); //第一行
            handleFirstLine(line);
            List<Map<String, Object>> temps = new ArrayList<>(1005);
            while ((line = br.readLine()) != null) {
                curLine++;
                Map<String, Object> OrderMaps = generateEsItem(line);
                temps.add(OrderMaps);
                if (curLine % 1000 == 0) {
                    service.addDocumentBatch(temps);
                    temps.clear();
                    System.out.println("has handled line: " + curLine);
                    checkPointLine = curLine;
                }
            }
            if (!temps.isEmpty()) {
                service.addDocumentBatch(temps);
            }
        } catch (Exception e) {
            System.out.println("error, curLine is " + curLine);
            System.out.println("checkPoint line is " + checkPointLine);
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void handleFirstLine(String line){
        String[] props = line.split("\t");
        for (int i = 0; i < props.length; i++) {
            System.out.println(i + " " + props[i]);
        }
    }

    Map<String,Object> generateEsItem(String line){
        String[] values = line.split("\t");
        String rowkey = values[0];
        String[] split = rowkey.split("\\+");
        String user_log_acct = split[1];
        Date sale_ord_tm = new Date(Long.parseLong(split[2]));
        String sale_ord_idStr = split[3];
        Long sale_ord_id = Long.parseLong(sale_ord_idStr);
        String item_name = values[1];
        String brandname = values[2];
        String cancel_flagStr = values[6];
        Boolean cancel_flag = cancel_flagStr.equals("1");
        String finish_flagStr = values[7];
        Boolean finish_flag = finish_flagStr.equals("1");
        String delete_flagStr = values[8];
        Boolean delete_flag = delete_flagStr.equals("1");

        Order order = new Order(sale_ord_id, user_log_acct, sale_ord_tm, item_name, brandname, cancel_flag, finish_flag, delete_flag);
        Map<String, Object> map = new HashMap<>();
        map.put("rowkey", rowkey);
        map.put("order", order);
        return map;
    }
}
