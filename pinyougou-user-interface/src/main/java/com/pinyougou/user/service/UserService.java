package com.pinyougou.user.service;
import com.pinyougou.pojo.TbUser;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	
	
	
	/**
	 * 注册用户
	 * @param user
	 */
	public void add(TbUser user);
	
	/**
	 * 生成短信验证码
	 * @param phone
	 */
	public void createSmsCode(String phone);
	
	
}
