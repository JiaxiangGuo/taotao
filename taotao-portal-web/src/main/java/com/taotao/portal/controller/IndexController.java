package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
/**
 * 首页展示
 * @author Guojiaxiang
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import com.taotao.utils.JsonUtils;
/*
 * 首页展示
 */

@Controller
public class IndexController {
	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	@Value("${AD1_WEDTH}")
	private Integer AD1_WEDTH;
	@Value("${AD1_WEDTH_B}")
	private Integer AD1_WEDTH_B;
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	
	@Autowired
	private ContentService contentService;
	@RequestMapping("/index")
	public String showIndex(Model model){
		//根据CategoryId查询轮播图内容列表
		List<TbContent> contentList = contentService.getContentByCategoryId(AD1_CATEGORY_ID);
		List<AD1Node> ad1Nodes = new ArrayList<>();
		for (TbContent content : contentList) {
			AD1Node node = new AD1Node();
			node.setAlt(content.getTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setWidth(AD1_WEDTH);
			node.setWidthB(AD1_WEDTH_B);
			node.setSrc(content.getPic());
			node.setSrcB(content.getPic2());
			node.setHref(content.getUrl());
			ad1Nodes.add(node);
		}
		//转成json数据
		String ad1Json = JsonUtils.objectToJson(ad1Nodes);
		//将json传递给页面
		model.addAttribute("ad1", ad1Json);
		return "index";
	}
}
