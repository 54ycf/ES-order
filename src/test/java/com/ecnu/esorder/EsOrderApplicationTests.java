package com.ecnu.esorder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EsOrderApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private ElasticsearchClient client;


    @Test
    public void queryCountByFiled() throws Exception {
        CountResponse count = client.count(c -> c.index("order").query(q -> q.term(t -> t
                .field("rowkey")
                .value("ycfcfycf")
        )));
        long total = count.count();
        System.out.println(total);
    }


    @Test
    public void existsTest() throws IOException {
//        BooleanResponse booleanResponse = client.indices().exists(e -> e.index("user"));
//        System.out.println(booleanResponse.value());

        CountResponse count = client.count(c -> c.index("order").query(q -> q.term(t -> t
                .field("rowkey")
                .value("ycfcfycf")
        )));
        long total = count.count();
        System.out.println(total);
    }
}
