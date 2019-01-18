package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbContentCategory;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

//@Service(timeout = 5000)在applicationcontent-service已配置  此处可不配
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public Map<String, Object> search(Map searchMap) {
		
		/**
		Query query=new SimpleQuery();
		//添加查询条件
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		map.put("rows", page.getContent());
		*/
		
	
		Map<String,Object> map=new HashMap<>();
		
		//1.根据关键字：高亮查询
		map.putAll(searchList(searchMap));
		
		//2.分组查询：商品分类列表
		List<String> categoryList = searchCategoryList(searchMap);
		map.put("categoryList", categoryList);
		
		//3.查询品牌和规格列表
		String category=(String)searchMap.get("category");
		if(!"".equals(category)) {//如果有分类名称
			map.putAll(searchBrandAndSpecList(categoryList.get(0)));
		}else {//如果没有分类名称，按照第一个查询
			if(categoryList.size()>0){
				map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			}
		}
		
	
		return map;
		
	}
	
	//查询列表
	private Map searchList(Map searchMap){
		
		//高亮选项初始化
		HighlightQuery query = new SimpleHighlightQuery();
		HighlightOptions highlightOptions=new HighlightOptions().addField("item_title");//设置高亮的域
		highlightOptions.setSimplePrefix("<em style='color:red'>");//高亮前缀
		highlightOptions.setSimplePostfix("</em>");//高亮后缀
		query.setHighlightOptions(highlightOptions);
		
		//1.1关键字查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//1.2按商品分类过滤
		if (!"".equals(searchMap.get("category"))) {//如果用户选择了筛选
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//1.3按品牌过滤
		if (!"".equals(searchMap.get("brand"))) {//如果用户选择了筛选
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		
		//1.4过滤规格    因为动态域*传入中文变成下划线此功能暂时不好用
		if(searchMap.get("spec")!=null){
			Map<String,String> specMap= (Map) searchMap.get("spec");
			for(String key:specMap.keySet() ){
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);				
			}			
		}
		
		//1.6 分页查询		
		Integer pageNo= (Integer) searchMap.get("pageNo");//获取页码
		if(pageNo==null){
			pageNo=1;//默认第一页
		}
		Integer pageSize=(Integer) searchMap.get("pageSize");//每页记录数 
		if(pageSize==null){
			pageSize=20;//默认20
		}
		query.setOffset((pageNo-1)*pageSize);//起始索引
		query.setRows(pageSize);//设置每页记录数
		
		
		
		
		//返回高亮页对象
		HighlightPage<TbItem> page=solrTemplate.queryForHighlightPage(query, TbItem.class);
		
		for(HighlightEntry<TbItem> entry: page.getHighlighted()){//循环高亮入口集合
			
			List<Highlight> HighlightList = entry.getHighlights();//获取高亮列表（高亮域的个数）		
			/*for (Highlight h : HighlightList) {
				List<String> list = h.getSnipplets();//每个域可能存多值
				System.out.println(list);
			}*/
			if (entry.getHighlights().size()>0 && entry.getHighlights().get(0).getSnipplets().size()>0) {
				TbItem item = entry.getEntity();//获取原实体类
				item.setTitle(HighlightList.get(0).getSnipplets().get(0));
			}
					
		}
		Map<String,Object> map=new HashMap<>();
		map.put("rows",page.getContent());
		map.put("totalPages", page.getTotalPages());//返回总页数给前端
		map.put("total", page.getTotalElements());//返回总记录数给前端
		return map;
	}
	
	/**
	 * 查询分组（查询商品分类列表）
	 * @param searchMap
	 * @return
	 */
	private  List searchCategoryList(Map searchMap){
		List<String> list=new ArrayList();
		
		Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//设置分组选项
		GroupOptions groupOptions=new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		
		//得到分组页
		GroupPage<TbItem> page=solrTemplate.queryForGroupPage(query, TbItem.class);
		
		//根据列得到分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		
		//得到分组结果入口页
		Page<GroupEntry<TbItem>> groupEntries  = groupResult.getGroupEntries();
		
		//得到分组入口集合
		List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
		
		for (GroupEntry<TbItem> groupEntry : entryList) {
			list.add(groupEntry.getGroupValue());//将分组结果的名称封装到返回值中
		}
		
		return list;
	}
	
	//根据商品分类名称查询品牌和规格列表
	private Map searchBrandAndSpecList(String category){
		Map map=new HashMap();		
		//根据商品分类名称获取模板ID
		Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
		if(templateId!=null){
			//根据模板ID查询品牌列表 
			List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
			map.put("brandList", brandList);//返回值添加品牌列表
			//根据模板ID查询规格列表
			List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
			map.put("specList", specList);				
		}			
		return map;
	}
}
