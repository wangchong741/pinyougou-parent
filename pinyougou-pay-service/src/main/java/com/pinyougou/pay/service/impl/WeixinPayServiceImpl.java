package com.pinyougou.pay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeiXinPayService;

import util.HttpClient;

/**
 * 微信支付接口实现类
 * 
 * @author wangchong 2019年2月15日
 */
@Service
public class WeixinPayServiceImpl implements WeiXinPayService {
	@Value("${appid}")
	private String appid;
	@Value("${partner}")
	private String partner;
	@Value("${partnerkey}")
	private String partnerkey;
	
	
	@Override
	public Map createNative(String out_trade_no, String total_fee) {	
		// 1.封装参数
		Map paramMap = new HashMap<>();
		paramMap.put("appid", appid);//公众账号ID
		paramMap.put("mch_id", partner);//商户号
		paramMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串 32位
		paramMap.put("body", "品优购");//商品描述
		paramMap.put("out_trade_no", out_trade_no);//商户订单号
		paramMap.put("total_fee", total_fee);//订单金额
		paramMap.put("spbill_create_ip", "127.0.0.1");//终端IP
		paramMap.put("notify_url", "http://test.itcast.cn");//回调地址(随便写)
		paramMap.put("trade_type", "NATIVE");//交易类型
		try {
			String xmlParam =WXPayUtil.generateSignedXml(paramMap, partnerkey);
			System.out.println("微信支付请求参数"+xmlParam);
			// 2.发送请求
			HttpClient client=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			client.setHttps(true);
			client.setXmlParam(xmlParam);
			client.post();		
			
			// 3.获取结果
			String result = client.getContent();
			
			Map<String, String> resultMap = WXPayUtil.xmlToMap(result);		
			System.out.println("微信返回结果"+resultMap);
			Map<String, String> returnMap=new HashMap<>();
			returnMap.put("code_url","www.baidu.com");
			returnMap.put("out_trade_no",out_trade_no);//订单号
			returnMap.put("total_fee", total_fee);//总金额
			return returnMap;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<>();
		}
	}


	@Override
	public Map queryPayStatus(String out_trade_no) {
		Map map = new HashMap<>();
		if(out_trade_no.length()>0) {
			map.put("trade_state", "SUCCESS");
		}
		return map;
	}


	@Override
	public Map closePay(String out_trade_no) {
		Map<String, String> map = new HashMap<>();
		map.put("111", "111");
		return map;
	}
		
	
}
