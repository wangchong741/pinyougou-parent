package com.pinyougou.mapper;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;

public interface TbBrandMapper {
  
    List<TbBrand> selectByExample(TbBrandExample example);
    
    int insert(TbBrand record);
    
    TbBrand selectByPrimaryKey(Long id);
    
    int updateByPrimaryKey(TbBrand record);
    
    int deleteByPrimaryKey(Long id);
    
    List<Map> selectOptionList();

}