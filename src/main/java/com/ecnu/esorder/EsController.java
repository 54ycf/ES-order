package com.ecnu.esorder;

import com.ecnu.esorder.dao.EsDAO;
import com.ecnu.esorder.param.FlagSearchParam;
import com.ecnu.esorder.param.KeySearchParam;
import com.ecnu.esorder.param.RowKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/es")
public class EsController {
    @Autowired
    EsDAO esDAO;

    @PostMapping("/keyword")
    public List<String> searchByName(@RequestBody KeySearchParam param) throws IOException {
        return esDAO.searchByName(param.getKeyword(), param.getUser_log_acct());
    }

    @PostMapping("/status/finish")
    public List<String> searchFinish(@RequestBody FlagSearchParam param) throws IOException {
        return esDAO.searchByFlag(param.getUser_log_acct(), false, true, param.getOffset());
    }

    @PostMapping("/status/wait")
    public List<String> searchWait(@RequestBody FlagSearchParam param) throws IOException {
        return esDAO.searchByFlag(param.getUser_log_acct(), false, false, param.getOffset());
    }

    @PostMapping("/status/cancel")
    public List<String> searchCancel(@RequestBody FlagSearchParam param) throws IOException {
        return esDAO.searchByFlag(param.getUser_log_acct(), true, false, param.getOffset());
    }

    @PostMapping("/delete")
    public int deleteOrder(@RequestBody RowKey rowkey){
        try {
            esDAO.updateFlagById("delete_flag", true, rowkey.getRowKey());
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }

    @PostMapping("/confirm")
    public int confirmOrder(@RequestBody RowKey rowkey){
        try {
            esDAO.updateFlagById("finish_flag", true, rowkey.getRowKey());
            return 0;
        } catch (IOException e) {
            return 1;
        }
    }


}
