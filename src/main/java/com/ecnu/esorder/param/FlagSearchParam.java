package com.ecnu.esorder.param;

import lombok.Data;

@Data
public class FlagSearchParam {
    private String user_log_acct;
    private Integer offset;
}