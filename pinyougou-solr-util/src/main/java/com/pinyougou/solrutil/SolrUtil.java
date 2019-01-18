package com.pinyougou.solrutil;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;

/**
 * 往solr中导入数据
 * @author wangchong
 *
 */
// 加上@Component相当于同一个bean
@Component 
public class SolrUtil {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	/**
	 * 导入商品数据
	 */
	public void importItemData(){
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");//审核通过的才导入
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===商品列表===");
		for (TbItem tbItem : itemList) {
			System.out.println("商品标题： "+tbItem.getTitle()+",分类："+tbItem.getCategory());
			
			Map specMap= JSON.parseObject(tbItem.getSpec(),Map.class);//将spec字段中的json字符串转换为map
			tbItem.setSpecMap(specMap);//给带注解的字段赋值
			System.out.println(specMap);
		}
		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
		
	}
	
	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
		SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
		solrUtil.importItemData();
	}

}
