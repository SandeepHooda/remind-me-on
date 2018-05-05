APP.CONTROLLERS.controller ('CTRL_Login',['$scope',
    function($scope){
	var theCtrl = this;
	
	if (window.localStorage.getItem('regID')){
		
	}else {
		if (document.URL.indexOf("?") > 0) {
			let splitURL = document.URL.split("?");
			let splitParams = splitURL[1].split("&");
			let singleURLParam = splitParams[0].split('=');
			alert(singleURLParam[1].split('#')[0])
	
		}
	}
	$scope.showMenu = function () {
	    $ionicSideMenuDelegate.toggleLeft();
	};
	 
	 theCtrl.signIN = function(){
		 window.open("/Oauth", "_self");
	 }
	  
	  
}
])