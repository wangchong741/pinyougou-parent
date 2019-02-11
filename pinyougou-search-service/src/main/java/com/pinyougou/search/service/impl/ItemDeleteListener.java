package com.pinyougou.search.service.impl;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.search.service.ItemSearchService;

/**
 * 从索引库删除消息监听类
 * 
 * @author wangchong 2019年2月11日
 */
@Component
public class ItemDeleteListener implements MessageListener {

	@Autowired
	private ItemSearchService itemSearchService;

	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] goodsIds = (Long[]) objectMessage.getObject();
			System.out.println("ItemDeleteListener监听接收到消息..." + goodsIds);
			itemSearchService.deleteByGoodsIds(Arrays.asList(goodsIds));
			System.out.println("成功删除索引库中的记录");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
