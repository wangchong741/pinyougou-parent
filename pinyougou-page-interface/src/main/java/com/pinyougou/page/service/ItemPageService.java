package com.pinyougou.page.service;

/**
 * 商品详细页接口
 * 
 * @author wangchong 2019年1月30日
 */
public interface ItemPageService {
	
	/**
	 * 生成商品详细页
	 * 
	 * @param goodsId
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);

}
