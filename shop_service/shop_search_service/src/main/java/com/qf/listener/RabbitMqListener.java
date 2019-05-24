package com.qf.listener;

import com.qf.entity.Goods;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @version 1.0
 * @user ken
 * @date 2019/5/24 15:23
 */
@Component
public class RabbitMqListener {

    @Autowired
    private SolrClient solrClient;

    @RabbitListener(queues = "search_queue")
    public void goodsMsgHander(Goods goods){
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", goods.getId() + "");
        document.addField("gname", goods.getGname());
        document.addField("ginfo", goods.getGinfo());
        document.addField("gprice", goods.getGprice().floatValue());
        document.addField("gsave", goods.getGsave());
        document.addField("gimages", goods.getGimages());

        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
