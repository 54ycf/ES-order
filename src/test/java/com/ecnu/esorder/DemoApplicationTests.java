package com.ecnu.esorder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

    @Autowired
    private ElasticsearchClient client;

    @Test
    public void createTest() throws IOException {
        log.info("创建索引");
        //写法比RestHighLevelClient更加简洁
        CreateIndexResponse indexResponse = client.indices().create(c -> c.index("order"));
        log.info(String.valueOf(indexResponse));
    }

    @Test
    public void getCompanyList() throws IOException {
 		log.info("查询索引");	
        GetIndexResponse getIndexResponse = client.indices().get(i -> i.index("order"));
        log.info(String.valueOf(getIndexResponse));
    }

    @Test
    public void existsTest() throws IOException {
        log.info("测试index是否存在");
        BooleanResponse booleanResponse = client.indices().exists(e -> e.index("order"));
        log.info(String.valueOf(booleanResponse.value()));
    }

//    @Test
//    public void userInsert() throws IOException {
//        log.info("user信息插入");
//        //创建user列表
//        List<User> users = new ArrayList<>();
//        User user = new User();
//        user.setId("18");
//        user.setName("李三");
//        user.setAge(13);
//        user.setSex("男");
//        users.add(user);
//
//        List<BulkOperation> bulkOperations = new ArrayList<>();
//        //将user中id作为es id，也可不指定id es会自动生成id
//        users.forEach(a -> bulkOperations.add(BulkOperation.of(b -> b.index(c -> c.id(a.getId()).document(a)))));
//        client.bulk(x ->x.index("user").operations(bulkOperations));
//
//    }

}
