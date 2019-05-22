package com.qf.shop_search_service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopSearchServiceApplicationTests {


    @Autowired
    private SolrClient solrClient;


    //id存在就是修改 id不存在就是添加
    @Test
    public void insertData() throws IOException, SolrServerException {
        for (int i = 0; i < 10; i++) {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", i + 1);
            if(i == 5){
                document.addField("gname", "华为华为手机" + i);
            } else {
                document.addField("gname", "华为手机" + i);
            }

            document.addField("ginfo", "性价比很高的国产手机，国产手机中的战斗机" + i);
            document.addField("gprice", 9.9 + i);
            document.addField("gsave", 1000 + i);
            document.addField("gimages", "http://www.baidu.com");

            solrClient.add(document);
        }

        solrClient.commit();
    }

    @Test
    public void deleteData() throws IOException, SolrServerException {
//        solrClient.deleteById("1");
        solrClient.deleteByQuery("gname:华为");
        solrClient.commit();
    }

    @Test
    public void query() throws IOException, SolrServerException {
        SolrQuery solrQuery = new SolrQuery("gname:华为");
        //获取查询结果
        QueryResponse query = solrClient.query(solrQuery);
        //获得查询结果集
        SolrDocumentList results = query.getResults();

        for (SolrDocument result : results) {
            String id = (String) result.get("id");
            String gname = (String) result.get("gname");
            String ginfo = (String) result.get("ginfo");
            String gimages = (String) result.get("gimages");
            float gprice = (float) result.get("gprice");

            System.out.println("-->" + id + " " + gname + " " + ginfo + " " + gimages + " " + gprice);
        }
    }
}
