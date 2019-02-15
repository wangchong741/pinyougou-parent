package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pinyougou.pojogroup.Cart;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestRedis {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testSetValue() {
		List list = new ArrayList<>();
		list.add("aaa");
		list.add("bbb");
		redisTemplate.boundHashOps("cartList").put("w", list);
	}

	@Test
	public void testGetValueByKey() {
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get("w");
		System.out.println(cartList);
	}

	// 删除缓存中的值
	@Test
	public void deleteValue() {
		redisTemplate.delete("cartList");
	}

}
