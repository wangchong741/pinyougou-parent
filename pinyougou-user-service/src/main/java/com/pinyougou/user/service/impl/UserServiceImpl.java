package com.pinyougou.user.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;

/**
 * 服务实现层
 * 
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 用户注册
	 */
	@Override
	public void add(TbUser user) {

		user.setCreated(new Date());// 用户注册时间
		user.setUpdated(new Date());// 修改时间
		user.setSourceType("1");// 注册来源
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));// 密码加密

		userMapper.insert(user);
	}

	
	@Autowired
	private JmsTemplate jmsTemplate;	
	@Autowired
	private Destination smsDestination;
	/**
	 * 发端短信验证码
	 */
	@Override
	public void createSmsCode(final String phone) {
		// 1.生成6位随机数验证码
		String smscode = (long) (Math.random() * 1000000) + "";
		System.out.println("验证码：" + smscode);

		// 2,将验证码存入缓存
		redisTemplate.boundHashOps("smscode").put(phone, smscode);
		
		//3将短信内容发送到activeMQ
		jmsTemplate.send(smsDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("PhoneNumbers", phone);//手机号
				mapMessage.setString("SignName", "");//签名
				mapMessage.setString("TemplateCode", "SMS_157448823");//验证码模板
				//构建模板内容参数
				Map<String,String> parmMap=new HashMap<>();
				parmMap.put("TemplateParam", smscode);
				
				mapMessage.setString("TemplateParam", JSON.toJSONString(parmMap));//模板内容参数
				
				return mapMessage;
			}
			
		});

	}

	/**
	 * 校验验证码
	 */
	@Override
	public boolean checkSmsCode(String phone, String code) {
		String systemCode=(String) redisTemplate.boundHashOps("smscode").get(phone);
		if(systemCode==null){
			return false;
		}
		if (!systemCode.equals(code)) {
			return false;
		}
		return true;
	}

}
