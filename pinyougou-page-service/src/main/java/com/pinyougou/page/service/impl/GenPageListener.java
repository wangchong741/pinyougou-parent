package com.pinyougou.page.service.impl;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pinyougou.page.service.ItemPageService;

/**
 * 生成页面消息监听类
 * 
 * @author wangchong 2019年2月11日
 */

@Component
public class GenPageListener implements MessageListener {

	@Autowired
	private ItemPageService itemPageService;

	@Override
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			System.out.println("接收到消息：" + text);
			boolean b = itemPageService.genItemHtml(Long.parseLong(text));
			System.out.println("网页生成是否成功:"+b);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
