APP.CONTROLLERS.controller ('CTRL_Expired',['$scope','$ionicSideMenuDelegate','$state',
    function($scope, $ionicSideMenuDelegate,$state){
	var theCtrl = this;
	
	if (!window.localStorage.getItem('regID')){
		$state.transitionTo('menu.login');
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