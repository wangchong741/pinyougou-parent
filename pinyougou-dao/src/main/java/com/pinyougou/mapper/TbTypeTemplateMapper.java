package com.pinyougou.mapper;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.pojo.TbTypeTemplateExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TbTypeTemplateMapper {

    int deleteByPrimaryKey(Long id);

    int insert(TbTypeTemplate record);

    List<TbTypeTemplate> selectByExample(TbTypeTemplateExample example);

    TbTypeTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKey(TbTypeTemplate record);
    
    List<Map> selectTypeTemplateList();
    

}