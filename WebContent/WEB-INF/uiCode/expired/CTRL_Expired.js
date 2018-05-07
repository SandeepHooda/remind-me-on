APP.CONTROLLERS.controller ('CTRL_Expired',['$scope','$ionicSideMenuDelegate','$state','$http','$rootScope',
    function($scope, $ionicSideMenuDelegate,$state,$http,$rootScope){
	var theCtrl = this;
	var regID = window.localStorage.getItem('regID');
	if (document.URL.indexOf('localhost')>=0){
		regID = "0457b82f-a156-4946-93cc-c73fae5b9e8a";
		 window.localStorage.setItem('regID', regID);
	}
	
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
	  			}else {
	  				$scope.$emit('getMyRemindersList');
	  			}
	  		},
			function(response){
				$state.transitionTo('menu.login');
				
			});
	}
	
	$rootScope.$on('logOut',function(event){
		 regIDStorege = window.localStorage.getItem('regID');
		 $http.get('/ws/logout/'+regIDStorege)
	  		.then(function(response){
	  			if (!response.data){//Reg id don't exist in DB - after delete 
	  				window.localStorage.setItem('regID', 'invalid');
	  				localStorage.removeItem('name');
	  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	  				$state.transitionTo('menu.login');
	  			}
	  		},
			function(response){
	  			//Failed to log out
				
			});
		});
	
	
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