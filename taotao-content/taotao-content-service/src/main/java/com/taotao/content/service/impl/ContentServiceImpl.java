package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	/*
	 *分页查询分类内容
	 */
	public EasyUIDataGridResult getContentListByCategoryId(long categoryId, int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//取查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}
	/*
	 * 新增内容
	 */
	public TaotaoResult addContent(TbContent content){
		content.setId(IDUtils.getItemId());
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		return TaotaoResult.ok();
	}
	
	/*
	 * 编辑内容
	 */
	public TaotaoResult editContent(TbContent content){
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		return TaotaoResult.ok();
	}
	/*
	 * 删除内容
	 */
	public TaotaoResult delContent(Long[] ids){
		for (Long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
		}
		return TaotaoResult.ok();
	}
/*	public static void main(String[] args) {
		ContentServiceImpl contentServiceImpl = new ContentServiceImpl();
		contentServiceImpl.getContentListByCategoryId(89, 1, 20);
	}*/
}
