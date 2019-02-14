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
		// 获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("当前登录人："+username);
		try {
			List<Cart> cartList = findCartList();// 从cookie中获取购物车列表
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);// 调用服务方法操作购物车
			if (username.equals("anonymousUser")) {// 如果未登录
				
				util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24,
						"UTF-8");// 将新的购物车存入cookie
				System.out.println("向cookie存入数据");
			} else {// 如果已登录
				cartService.saveCartListToRedis(username, cartList);
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
		if (username.equals("anonymousUser")) {// 如果未登录
			//// 从cookie中提取购物车
			String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
			if (cartListString == null || cartListString.equals("")) {
				cartListString = "[]";
			}
			List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
			return cartList_cookie;
		} else {// 如果已登录
			List<Cart> cartList_redis = cartService.findCartListFromRedis(username);// 从redis中提取
			return cartList_redis;
		}
	}

}
