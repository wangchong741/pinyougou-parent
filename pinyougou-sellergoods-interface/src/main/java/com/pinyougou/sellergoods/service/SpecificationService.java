package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojogroup.Specification;

import entity.PageResult;

/**
 * 服务层接口
 * 
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * 
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	/**
	 * 分页+条件查询（条件封装在对象里）
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbSpecification specification, int pageNum,int pageSize);
	
	public void add(Specification specification);
	
	public Specification findOne(Long id);
	
	public void update(Specification specification);
	
	public void delete(Long[] ids);
	
	/**
	 * 返回规格下拉列表数据
	 * @return
	 */
	public List<Map> selectOptionList();

}
