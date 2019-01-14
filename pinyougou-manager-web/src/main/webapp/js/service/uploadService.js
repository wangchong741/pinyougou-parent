//文件上传服务层
app.service("uploadService",function($http){
	
	this.uploadFile=function(){
		var formData=new FormData();
		//file 文件上传框name 
	    formData.append("file",file.files[0]);   
		return $http({
            url:"../upload.do",
            method:'POST',
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });		
	}	
	
	
});