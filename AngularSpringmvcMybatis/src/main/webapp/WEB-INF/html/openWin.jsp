<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="myApp" ng-controller="OpenController">
<head>
<script src="../js/angular/angular.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>openWin</title>
</head>
<body>
 	Hello Open...
   
</body>
<script>
		var app = angular.module("myApp", []);
		app.controller('OpenController', function($scope,$timeout, $http) {
			if(!window.scope){
				alert("No....");
			}
			
		});
		
				
	
	</script>
</html>