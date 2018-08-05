<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="myApp" ng-controller="DemoController">
<head>
<script src="../js/angular/angular.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
 Test你好
   Your name: <input type="text" ng-model="yourname" placeholder="World" ></input>
 <a ng-click='openWin()'>open</a>
</body>
<script>
		var app = angular.module("myApp", []);
		app.controller('DemoController', function($scope,$timeout, $http) {
			$scope.yourname = "xxx";
			
			$scope.openWin = function(){
				var ow = window.open("open.do", "", "location=no");
				ow.scope = $scope;
			}
			
			
			$http({
				url:'test.do?name=query',
				method:'POST'
				}).then(function successCallback(response) {
				    // this callback will be called asynchronously
				    // when the response is available
				    debugger;
				  }, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				  });;
		});
		
				
	
	</script>
</html>