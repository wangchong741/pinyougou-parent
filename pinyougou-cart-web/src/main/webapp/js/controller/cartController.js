//购物车控制层 
app.controller('cartController',function($scope,cartService){
	//查询购物车列表
	$scope.findCartList=function(){
		cartService.findCartList().success(
			function(response){
				$scope.cartList=response;
			}
		);		
	}
	
	//添加商品到购物车(数量加减)
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
			function(response){
				if(response.success){
					$scope.findCartList();//如果成功，刷新列表
				}else{
					alert(response.message);//弹出错误提示
				}				
			}
		);
	}
	
});