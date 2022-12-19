package com.ecnu.esorder.param;

import lombok.Data;

@Data
public class KeySearchParam {
    private String keyword;
    private String user_log_acct;
    private Integer offset; //第一次访问时为0，前端每成功访问一次就+1
}