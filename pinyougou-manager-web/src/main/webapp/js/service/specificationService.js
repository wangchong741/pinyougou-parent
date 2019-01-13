//规格服务层
app.service('specificationService',function($http){
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../specification/search.do?page='+page+"&rows="+rows, searchEntity);
	} 
	
	//增加 
	this.add=function(entity){
		return  $http.post('../specification/add.do',entity );
	}
	
	//修改 
	this.update=function(entity){
		return  $http.post('../specification/update.do',entity );
	}
	//删除
	this.dele=function(ids){
		return $http.get('../specification/delete.do?ids='+ids);
	}
	
	//查询实体
	this.findOne=function(id){
		return $http.get('../specification/findOne.do?id='+id);
	}

	//规格下拉列表数据
	this.selectOptionList=function(){
		return $http.get('../specification/selectOptionList.do');
	}
});