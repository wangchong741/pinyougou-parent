package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌接口：查询所有品牌
 * @author wangchong
 * 2018年12月21日
 */
public interface BrandService {

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<TbBrand> findAll();
	/**
	 * 测试
	 * @return
	 */
	public String showName();
	/**
	 * 返回分页列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	/**
	 * 增加品牌
	 * @param brand
	 */
	public void add(TbBrand brand);
	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id) ;
	/**
	 * 修改
	 */
	public void update(TbBrand brand);
	/**
	 * 根据id删除
	 * @param ids
	 */
	public void delete(Long[] ids);
	/**
	 * 返回分页列表(条件查询)
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(TbBrand brand,int pageNum,int pageSize);
	/**
	 * 返回下拉列表数据
	 * @return
	 */
	public List<Map> selectOptionList();
	
}
