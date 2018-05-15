package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.utils.JsonUtils;
/**
 * 监听商品添加事件,同步索引库
 * @author Guojiaxiang
 *
 */
public class MyMessageListener implements MessageListener{
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	public void onMessage(Message message) {
		//接收到消息
		TextMessage textMessage = (TextMessage) message;
		try {
			//从消息中获取商品id
			String id = textMessage.getText();
			long itemId = Long.parseLong(id);
			//等待事务提交
			Thread.sleep(1000);
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			//向文档中添加域
			document.addField("id",searchItem.getId());
			document.addField("item_category_name",searchItem.getCategory_name());
			document.addField("item_image",searchItem.getImage());
			document.addField("item_desc", searchItem.getItem_desc());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_title", searchItem.getTitle());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
