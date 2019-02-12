package com.pinyougou.user.service.impl;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.dubbo.config.annotation.Service;
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

	/**
	 * 发端短信验证码
	 */
	@Override
	public void createSmsCode(String phone) {
		// 1.生成6位随机数验证码
		String smsCode = (long) (Math.random() * 1000000) + "";
		System.out.println("验证码：" + smsCode);

		// 2,将验证码存入缓存
		redisTemplate.boundHashOps("smsCode").put(phone, smsCode);
		
		//3将短信内容发送到activeMQ
		

	}

}
