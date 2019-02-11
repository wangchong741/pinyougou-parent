package com.pinyougou.search.service.impl;

import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

/**
 * 导入索引库消息监听类
 * @author wangchong
 * 2019年2月11日
 */
@Component
public class ItemSearchListener implements MessageListener {
	
	@Autowired
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage=(TextMessage)message;			
			String text = textMessage.getText();
			System.out.println("监听接收到消息..."+text);
			
			List<TbItem> list = JSON.parseArray(text,TbItem.class);
			
			itemSearchService.importList(list);//导入	
			
			System.out.println("成功导入到索引库");
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}


}
