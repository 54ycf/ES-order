package com.ecnu.esorder.dao;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ecnu.esorder.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EsDAO {
    @Autowired
    private ElasticsearchClient client;
    private int pageSize =10;

    //增添一条记录
    public void addDocument(Order order, String rowkey) throws IOException {
        IndexResponse indexResponse = client.index(i -> i
                .index("order")
                .id(rowkey)
                .document(order));
        System.out.println(indexResponse.result().jsonValue());
    }

    //批量增添
    public void addDocumentBatch(List<Map<String,Object>> listMaps) throws IOException {

        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>(1005);
        List<String> rowkeyList = new ArrayList<>(1005);
        for (Map<String, Object> listMap : listMaps) {
            orderList.add((Order) listMap.get("order"));
            rowkeyList.add((String) listMap.get("rowkey"));
        }
        //add to bulk
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            int finalI = i;
            bulkOperationArrayList.add(BulkOperation.of(o->o.index(ind->ind.document(order).id(rowkeyList.get(finalI)))));
        }
        BulkResponse bulkResponse = client.bulk(b -> b.index("order")
                .operations(bulkOperationArrayList));
        System.out.println("bulkResponse error? " + bulkResponse.errors());
    }

    public void updateFlagById(String flag, Boolean flagVal, String rowkey) throws IOException {
        UpdateResponse<Map> updateResponse = client.update(u -> u
                        .index("order")
                        .id(rowkey)
                        .doc(new HashMap<String, Boolean>(){{put(flag, flagVal);}})
                , Map.class);
        System.out.println(updateResponse.result());
    }

    public void getRowkeyByOrderID(Long sale_ord_id) throws IOException {
        SearchResponse<Map> searchResponse = client.search(
                search -> search
                        .index("order")
                        .source(source -> source.fetch(false))
                        .query(q -> q
                                .term(term -> term
                                        .field("sale_ord_id").value(sale_ord_id)
                                )
                        )
                , Map.class);
        String rowkey = searchResponse.hits().hits().get(0).id();
        System.out.println(rowkey);


//        GetResponse<Map> getResponse = client.get(g -> g
//                        .index("order")
//                        .id(orderId)
//                        .source(s -> s
//                                .fields(new ArrayList<>(){{ add("rowkey");}})
//                        )
//                , Map.class);
//        System.out.println(getResponse.source().get("rowkey"));
    }

    public List<String> searchByName(String keyword, String user_log_acct) throws IOException{
        SearchResponse<Map> searchResponse = client.search(
                search -> search
                        .index("order")
                        .source(source -> source.fetch(false))
                        .query(q -> q
                                .bool(bool1 -> bool1
                                        .must(must -> {
                                                    must.term(term -> term.field("user_log_acct").value(user_log_acct));
                                                    return must;})
                                        .must(must -> must
                                                .bool(bool2 -> bool2
                                                        .should(should -> {
                                                            should.match(m -> m.field("item_name").analyzer("ik_max_word").query(keyword));
                                                            return should;})
                                                        .should(should -> {
                                                            should.match(m -> m.field("brandname").analyzer("ik_max_word").query(keyword));
                                                            return should;})))))
                        .from(0)
                        .size(8)
                        .sort(sort -> sort.field(f -> f.field("sale_ord_tm").order(SortOrder.Asc)))
                , Map.class);
        List<String> rowkeys = searchResponse.hits().hits().stream().map(Hit::id).toList();
        for (String rowkey : rowkeys) {
            System.out.println(rowkey);
        }
        System.out.println("that's all");
        return rowkeys;
    }



    public void searchByFlag(String user_log_acct, String flag, boolean b) throws IOException{
        SearchResponse<Map> searchResponse = client.search(search -> search
                        .index("order")
                        .source(source -> source.fetch(false))
                        .query(q -> q
                                .bool(bool -> bool
                                        .must(m -> {
                                            m.term(t -> t.field("user_log_acct").value(user_log_acct));
                                            return m;
                                        })
                                        .must(m -> {
                                            m.term(t -> t.field(flag).value(b));
                                            return m;
                                        })))
                        .from(0)
                        .size(8)
                        .sort(sort -> sort.field(f -> f.field("sale_ord_tm").order(SortOrder.Asc)))
                , Map.class);
        List<String> rowkeys = searchResponse.hits().hits().stream().map(Hit::id).toList();
        for (String rowkey : rowkeys) {
            System.out.println(rowkey);
        }
        System.out.println("that's all");
    }










    public List<String> searchByName(String keyword, String user_log_acct, int offset) throws IOException{
        SearchResponse<Map> searchResponse = client.search(
                search -> search
                        .index("order")
                        .source(source -> source.fetch(false))
                        .query(q -> q
                                .bool(bool1 -> bool1
                                        .must(must -> {
                                            must.term(term -> term.field("user_log_acct").value(user_log_acct));
                                            return must;})
                                        .must(must -> must
                                                .bool(bool2 -> bool2
                                                        .should(should -> {
                                                            should.match(m -> m.field("item_name").analyzer("ik_max_word").query(keyword));
                                                            return should;})
                                                        .should(should -> {
                                                            should.match(m -> m.field("brandname").analyzer("ik_max_word").query(keyword));
                                                            return should;})))))
                        .from(offset*pageSize)
                        .size((offset+1)*pageSize)
                        .sort(sort -> sort.field(f -> f.field("sale_ord_tm").order(SortOrder.Asc)))
                , Map.class);
        List<String> rowkeys = searchResponse.hits().hits().stream().map(Hit::id).collect(Collectors.toList());
        return rowkeys;
    }

    public List<String> searchByFlag(String user_log_acct, boolean cancel, boolean finish, int offset) throws IOException{
        SearchResponse<Map> searchResponse = client.search(search -> search
                        .index("order")
                        .source(source -> source.fetch(false))
                        .query(q -> q
                                .bool(bool -> bool
                                        .must(m -> {
                                            m.term(t -> t.field("user_log_acct").value(user_log_acct));
                                            return m;
                                        })
                                        .must(m -> {
                                            m.term(t -> t.field("cancel_flag").value(cancel));
                                            return m;
                                        })
                                        .must(m -> {
                                            m.term(t -> t.field("finish_flag").value(finish));
                                            return m;
                                        }).must(m -> {
                                            m.term(t -> t.field("delete_flag").value(false));
                                            return m;
                                        })))
                        .from(offset*pageSize)
                        .size((offset+1)*pageSize)
                        .sort(sort -> sort.field(f -> f.field("sale_ord_tm").order(SortOrder.Asc)))
                , Map.class);
        List<String> rowkeys = searchResponse.hits().hits().stream().map(Hit::id).collect(Collectors.toList());
        return rowkeys;
    }
}
