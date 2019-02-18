package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeiXinPayService weixinPayService;
	
	@Reference
	private OrderService orderService;

	@RequestMapping("/createNative")
	public Map createNative() {
		// 1.获取当前登录用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// 2.提取支付日志（从缓存 ）
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);
		// 3.调用微信支付接口
		if (payLog != null) {
			return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee() + "");
		} else {
			return new HashMap<>();
		}
	}

	/**
	 * 检查支付状态
	 * 
	 * @param out_trade_no
	 * @return
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no) {
		Result result = null;
		int x = 0;
		while (true) {
			try {
				Thread.sleep(7000);// 7秒后自動支付成功(沒有商家賬號，模拟支付)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 调用查询接口
			Map<String, String> map = weixinPayService.queryPayStatus(out_trade_no);
			if (map == null) {// 出错
				result = new Result(false, "支付出错");
				break;
			}
			if (map.get("trade_state").equals("SUCCESS")) {// 如果成功
				result = new Result(true, "支付成功");
				// 修改订单状态
				orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));
				break;
			}
			// 为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
			x++;
			if (x >= 100) {
				result = new Result(false, "二维码超时");
				break;
			}
		}
		return result;
	}

}
