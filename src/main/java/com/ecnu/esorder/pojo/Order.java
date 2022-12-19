package com.ecnu.esorder.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private Long sale_ord_id;

    private String user_log_acct;

    private Date sale_ord_tm;

    private String item_name;

    private String brandname;

    private Boolean cancel_flag;

    private Boolean finish_flag;

    private Boolean delete_flag;
}
