app.controller('searchController',function($scope,searchService){	
	
	//定义搜索对象的结构
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{} ,'price':''};
	
	
	//添加搜索项，改变searchMap的值
	$scope.addSearchItem=function(key,value){
		if(key=='category' || key=='brand' || key=='price'){//如果点击的是分类或者是品牌
			$scope.searchMap[key]=value;
		}else{ //用户点击的是规格
			$scope.searchMap.spec[key]=value;
		}	
		$scope.search();//在添加筛选条件时自动调用搜索方法
	}
	
	
	//撤销搜索项
	$scope.removeSearchItem=function(key){
		if(key=="category" ||  key=="brand" || key=='price'){//如果是分类或品牌
			$scope.searchMap[key]="";		
		}else{//否则是规格
			delete $scope.searchMap.spec[key];//移除此属性
		}	
		$scope.search();//在删除筛选条件时自动调用搜索方法
	}
	
	
	//搜索
	$scope.search=function(){
		searchService.search( $scope.searchMap ).success(
			function(response){						
				$scope.resultMap=response;//搜索返回的结果
			}
		);	
	}	
});