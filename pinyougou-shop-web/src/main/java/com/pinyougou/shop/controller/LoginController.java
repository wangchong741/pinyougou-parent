package com.pinyougou.shop.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

	@RequestMapping("name")
	public Map getName() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = new ConcurrentHashMap<>();
		map.put("loginName", name);
		return map;
	}

}
