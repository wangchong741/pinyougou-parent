package com.pinyougou.cart.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeiXinPayService;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeiXinPayService weixinPayService;
	
	
	@RequestMapping("/createNative")
	public Map createNative(){
		IdWorker idWorker = new IdWorker();
		return weixinPayService.createNative(idWorker.nextId()+"", "1");
	}

	/**
	 * 检查支付状态
	 * @param out_trade_no
	 * @return
	 */
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no){
		Result result=null;		
		int x=0;		
		while(true){
			try {
				Thread.sleep(5000);//间隔三秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			//调用查询接口
			Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
			if(map==null){//出错			
				result=new  Result(false, "支付出错");
				break;
			}			
			if(map.get("trade_state").equals("SUCCESS")){//如果成功				
				result=new  Result(true, "支付成功");
				break;
			}			
			//为了不让循环无休止地运行，我们定义一个循环变量，如果这个变量超过了这个值则退出循环，设置时间为5分钟
			x++;
			if(x>=100){
				result=new  Result(false, "二维码超时");
				break;
			}
		}
		return result;
	}
	
	
}
