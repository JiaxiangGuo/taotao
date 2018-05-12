package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.taotao.redis.JedisClient;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;

	/*
	 * 分页查询分类内容
	 */
	public EasyUIDataGridResult getContentListByCategoryId(long categoryId, int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		// 取查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());

		return result;
	}

	/*
	 * 新增内容
	 */
	public TaotaoResult addContent(TbContent content) {
		content.setId(IDUtils.getItemId());
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		// 删除对应的缓存信息
		jedisClient.hdel("INDEX_CONTENT", content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	/*
	 * 编辑内容
	 */
	public TaotaoResult editContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeySelective(content);
		// 删除对应的缓存信息
		jedisClient.hdel("INDEX_CONTENT", content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	/*
	 * 删除内容
	 */
	public TaotaoResult delContent(Long[] ids) {
		TbContent content = contentMapper.selectByPrimaryKey(ids[0]);
		for (Long id : ids) {
			contentMapper.deleteByPrimaryKey(id);
		}
		jedisClient.hdel("INDEX_CONTENT", content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	/*
	 * 通过categoryId查询内容
	 */
	public List<TbContent> getContentByCategoryId(Long categoryId) {
		// 先查询缓存redis
		// 查询添加缓存不能影响正常业务
		try {
			String string = jedisClient.hget("INDEX_CONTENT", categoryId.toString());
			if (StringUtils.isNotBlank(string)) {
				return JsonUtils.jsonToList(string, TbContent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 缓存没有，查询数据库
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		// 将数据写入缓存redis
		try {
			jedisClient.hset("INDEX_CONTENT", categoryId.toString(), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
