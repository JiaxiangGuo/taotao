package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	TaotaoResult getItemById(String id);
	TaotaoResult getItemDescById(String id);
	EasyUIDataGridResult getItemList(int page, int rows);
	TaotaoResult addItem(TbItem item, String desc);
	TaotaoResult updateItem(TbItem item, String desc);
	TaotaoResult changeItemStatus(String[] ids, int status);
	TbItem getItemById1(String id);
	TbItemDesc getItemDescById1(String id);
}
