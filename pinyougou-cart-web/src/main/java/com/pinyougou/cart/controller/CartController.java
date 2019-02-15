package com.pinyougou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.pojogroup.Cart;

import entity.Result;

/**
 * 购物车控制类
 * 
 * @author wangchong 2019年2月14日
 */
@RestController
@RequestMapping("/cart")
public class CartController {
	@Reference
	private CartService cartService;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	/**
	 * 添加商品到购物车
	 * 
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/addGoodsToCartList")
	public Result addGoodsToCartList(Long itemId, Integer num) {
		
		//接收跨域请求http://localhost:9105 接受哪个地址访问 若不考虑安全可以让所有的访问用*
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
		response.setHeader("Access-Control-Allow-Credentials", "true");//需要操作cookie时需要加这一行，上面地址也必须是特定的地址。
		
		// 获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人：" + username);
		try {
			List<Cart> cartList = findCartList();// 从cookie中获取购物车列表
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);// 调用服务方法操作购物车
			if (username.equals("anonymousUser")) {// 如果未登录
				util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24,
						"UTF-8");// 将新的购物车存入cookie
			} else {// 如果已登录
				cartService.saveCartListToRedis(username, cartList);
				System.out.println("已登录向redis存数据"+cartList);
			}
			return new Result(true, "添加购物车成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加购物车失败");
		}
	}

	/**
	 * 购物车列表
	 * 
	 * @return
	 */
	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {
		// 获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// 从cookie中提取购物车
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if (cartListString == null || cartListString.equals("")) {
			cartListString = "[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		
		if (username.equals("anonymousUser")) {// 如果未登录
			return cartList_cookie;
		} else {// 如果已登录
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);// 从redis中提取购物车
			System.out.println("取数据cartList_redis"+cartList_redis);
			if (cartList_cookie.size()>0) {//判断本地购物车存在数据执行合并逻辑
				List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);// 合并购物车
				cartService.saveCartListToRedis(username, cartList);//将合并后的购物车存入redis
				util.CookieUtil.deleteCookie(request, response, "cartList");//清除本地cookie的数据
				System.out.println("执行了合并购物车逻辑");
				return cartList;
			}
			return cartList_redis;
		}
	}

}
