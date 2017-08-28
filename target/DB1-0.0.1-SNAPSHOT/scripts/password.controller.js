var passwordApp = angular.module('PasswordApp',[]);

passwordApp.controller('PasswordController',function($scope, $http)
{
	
	 $http.get('http://localhost:8080/services/validate').
     then(function(response) {
         $scope.greeting = response.data;
     });

		$scope.save = function()
		{
			
			
		};
});
