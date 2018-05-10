package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	TaotaoResult addContentCategory(TbContentCategory contentCategory);
}
