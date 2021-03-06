package com.pinyougou.pay.service;

import java.util.Map;

/**
 * 微信支付接口
 * @author wangchong
 * 2019年2月15日
 */
public interface WeiXinPayService {
	
	/**
	 * 生成微信支付二维码
	 * @param out_trade_no 订单号
	 * @param total_fee 金额(分)
	 * @return
	 */
	public Map createNative(String out_trade_no,String total_fee);

	/**
	 * 查询支付订单
	 * @param out_trade_no
	 * @return
	 */
	public Map queryPayStatus(String out_trade_no);
	
	/**
	 * 关闭支付订单
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no);
	
}
