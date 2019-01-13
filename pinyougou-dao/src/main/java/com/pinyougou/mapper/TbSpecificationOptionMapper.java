package com.pinyougou.mapper;

import java.util.List;

import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;

public interface TbSpecificationOptionMapper {
	
	int insert(TbSpecificationOption specificationOption);
	
	List<TbSpecificationOption> selectByExample(TbSpecificationOptionExample specificationOptionExample);
	
	int deleteByExample(TbSpecificationOptionExample example);
}
