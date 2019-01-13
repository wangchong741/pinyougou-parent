package com.pinyougou.mapper;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;

public interface TbSpecificationMapper {
	
	List<TbSpecification> selectByExample(TbSpecificationExample example);
	
	int insert(TbSpecification specification);
	
	TbSpecification selectByPrimaryKey(Long id);
	
	int updateByPrimaryKey(TbSpecification record);
	
	int deleteByPrimaryKey(Long id);
	
	List<Map> selectOptionList();

}