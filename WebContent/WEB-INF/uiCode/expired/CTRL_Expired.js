APP.CONTROLLERS.controller ('CTRL_Expired',['$scope','$ionicSideMenuDelegate','$state',
    function($scope, $ionicSideMenuDelegate,$state){
	var theCtrl = this;
	
	if (!window.localStorage.getItem('regID')){
		$state.transitionTo('menu.login');
	}
	$scope.showMenu = function () {
	    $ionicSideMenuDelegate.toggleLeft();
	  };
	  
}
])