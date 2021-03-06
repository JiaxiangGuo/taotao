package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.swing.plaf.synth.SynthStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.container.page.PageHandler;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Destination destination;
	/*
	 * 通过id获取商品详情
	 */
	public TaotaoResult getItemById(String id) {
		TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(id));
		return TaotaoResult.ok(item);
	}
	public TbItem getItemById1(String id) {
		TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(id));
		return item;
	}
	
	/*
	 * 通过id获取商品详情
	 */
	public TaotaoResult getItemDescById(String id) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(Long.parseLong(id));
		return TaotaoResult.ok(itemDesc);
	}
	public TbItemDesc getItemDescById1(String id) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(Long.parseLong(id));
		return itemDesc;
	}
	
	
	/*
	 * 分页查询商品列表
	 * @see com.taotao.service.ItemService#getItemList(int, int)
	 */
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	
	/*
	 * 添加商品
	 */
	public TaotaoResult addItem(TbItem item, String desc) {
			//生成商品id
			final long itemId = IDUtils.getItemId();
			//补全item属性
			item.setId(itemId);
			//商品状态，1：正常，2：下架，3：删除
			item.setStatus((byte) 1);
			item.setCreated(new Date());
			item.setUpdated(new Date());
			//向商品表插入数据
			itemMapper.insert(item);
			//创建一个商品描述表对应的pojo
			TbItemDesc itemDesc = new TbItemDesc();
			//补全pojo属性
			itemDesc.setItemDesc(desc);
			itemDesc.setItemId(itemId);
			itemDesc.setCreated(new Date());
			itemDesc.setUpdated(new Date());
			//向商品描述表插入数据
			itemDescMapper.insert(itemDesc);
			jmsTemplate.send(destination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					return session.createTextMessage(itemId+"");
				}
			});
			//返回结果
			return TaotaoResult.ok();
	}
	
	/*
	 * 删除商品
	 */
	public TaotaoResult changeItemStatus(String[] ids, int status) {
		TbItem item = new TbItem();
		item.setStatus((byte)status);
		for (String id : ids) {
			item.setId(Long.parseLong(id));
			itemMapper.updateByPrimaryKeySelective(item);
		}
		return TaotaoResult.ok();
		
	}

	/*
	 * 编辑商品
	 */
	public TaotaoResult updateItem(TbItem item, String desc) {
		item.setUpdated(new Date());
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		return TaotaoResult.ok();
	}

}
