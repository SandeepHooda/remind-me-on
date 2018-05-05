APP.CONTROLLERS.controller ('CTRL_Login',['$scope','$state',
    function($scope,$state){
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
	 if (regID){
		 window.localStorage.setItem('regID', regID);
		 window.localStorage.setItem('name', getCookie('name'));
	 }

	 var name = window.localStorage.getItem('name');
		if (name ){
			$scope.userName = "Welcome "+name;
		}else {
			$scope.userName ="Hello Guest";
		}
	
	if (window.localStorage.getItem('regID')){
		$state.transitionTo('menu.tab.home');
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