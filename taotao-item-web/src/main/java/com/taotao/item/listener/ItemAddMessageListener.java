package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Override
	public void onMessage(Message message) {
		//从消息中取商品id
		TextMessage textMessage = (TextMessage) message;
		String itemId;
		try {
			itemId = textMessage.getText();
			Thread.sleep(1000);
			//根据商品id查询商品信息及商品描述
			TbItem tbItem = itemService.getItemById1(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById1(itemId);
			//使用freemarker生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			//加载模板对象
			Template template = configuration.getTemplate("item.ftl");
			//准备模板需要的数据
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			//指定输出的目录及文件名
			Writer out = new FileWriter(new File("D:/repository/taotao/taotao-item-web/src/main/webapp/WEB-INF/html/"+itemId+".html"));
			//生成静态页面
			template.process(data, out);
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
