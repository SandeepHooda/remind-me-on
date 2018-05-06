APP.CONTROLLERS.controller ('CTRL_Login',['$scope','$state','$http',
    function($scope,$state,$http){
	var theCtrl = this;
	
	function getCookie(cname) {
	    var name = cname + "=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	var regID = getCookie('regID');
	var regIDStorege = window.localStorage.getItem('regID');
	 if (regID && regIDStorege != 'invalid'){
		 window.localStorage.setItem('regID', regID);
		 window.localStorage.setItem('name', getCookie('name'));
	 }

	 var name = window.localStorage.getItem('name');
		if (name ){
			$scope.userName = "Welcome "+name;
		}else {
			$scope.userName ="Hello Guest";
		}
	
	if (regIDStorege && regIDStorege != 'invalid' ){
		$state.transitionTo('menu.tab.home');
	}else if (regIDStorege == 'invalid'){
		localStorage.removeItem('regID');
	}
	$scope.showMenu = function () {
	    $ionicSideMenuDelegate.toggleLeft();
	};
	 
	 theCtrl.signIN = function(){
		 if(window.localStorage.getItem('regID')){
			 localStorage.removeItem('regID');
			 localStorage.removeItem('name');
		 }
		 
		 window.open("/Oauth", "_self");
	 }
	 
	 
	
	 
	  
}
])