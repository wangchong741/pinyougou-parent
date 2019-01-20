package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {

	/**
	 * 搜索
	 * @param keywords
	 * @return
	 */
	public Map<String,Object> search(Map searchMap);
	/**
	 * 导入列表
	 * @param list
	 */
	public void importList(List list);
	/**
	 * 删除索引库数据
	 * @param goodsIdList
	 */
	public void deleteByGoodsIds(List goodsIdList);
}
