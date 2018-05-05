APP.CONTROLLERS.controller ('CTRL_Expired',['$scope','$ionicSideMenuDelegate','$state','$http',
    function($scope, $ionicSideMenuDelegate,$state,$http){
	var theCtrl = this;
	var regID = window.localStorage.getItem('regID');
	if (!regID){
		$state.transitionTo('menu.login');
	}else {
		 $http.get('/ws/login/validate/'+regID)
	  		.then(function(response){
	  			if (!response.data){
	  				window.localStorage.setItem('regID', 'invalid');
	  				localStorage.removeItem('name');
	  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				$state.transitionTo('menu.login');
	  			}
	  		},
			function(response){
				$state.transitionTo('menu.login');
				
			});
	}
	var name = window.localStorage.getItem('name');
	if (name ){
		$scope.userName = "Welcome "+name;
	}else {
		$scope.userName ="Hello Guest";
	}
	
	$scope.showMenu = function () {
		if(document.URL.indexOf('/menu/login') <0){//Disable hanburger in log in state
			 $ionicSideMenuDelegate.toggleLeft();
		}
	   
	  };
	  
}
])