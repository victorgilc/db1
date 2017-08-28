(function() 
{
	  'use strict';
	var app = angular.module('PasswordApp',[]);

	app.controller('PasswordController', ['$scope','$http', passwordController]);


	function passwordController($scope, $http)
	{
	
		$scope.validatePassword = function(passForm)
		{
			passForm.$submitted = true;
			if(!$scope.myPasswrod)
			{
				return;
			}
			var config = {
				    params: {
				    	password: $scope.myPasswrod
				    }
				}
			
			
			 $http.get('http://localhost:8080/db1/validator/validate', config).then(
			     function(response) {
		           $scope.greeting = response.data;
			     }, function(error){
			    	 console.log(error);
			     });
				
		};
	}
}());
