package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	/*
	 * 通过id获取商品详情
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public TaotaoResult getItemById(@PathVariable String id){
		return itemService.getItemById(id);
	}
	/*
	 * 通过id获取商品描述
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public TaotaoResult getItemDescById(@PathVariable String id){
		return itemService.getItemDescById(id);
	}
	
	/*
	 * 分页查询商品列表
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		return itemService.getItemList(page, rows);
	}
	
	/*
	 *添加商品 
	 */
	@RequestMapping("/item/save") 
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc){
		return itemService.addItem(item, desc);
	}
	
	/*
	 *编辑商品 
	 */
	@RequestMapping("rest/item/update") 
	@ResponseBody
	public TaotaoResult updateItem(TbItem item, String desc){
		return itemService.updateItem(item, desc);
	}


	/*
	 *删除商品 
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public TaotaoResult delItem(String[] ids){
		return itemService.changeItemStatus(ids, 3);
	}
	
	/*
	 *下架商品 
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public TaotaoResult offItem(String[] ids){
		return itemService.changeItemStatus(ids, 2);
	}
	
	/*
	 *上架商品 
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public TaotaoResult reshelfItem(String[] ids){
		return itemService.changeItemStatus(ids, 1);
	}
}
