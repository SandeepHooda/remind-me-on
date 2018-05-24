APP.CONTROLLERS.controller ('CTRL_Login',['$scope','$state','$http','$ionicLoading','appData',
    function($scope,$state,$http,$ionicLoading,appData){
	var theCtrl = this;
	$scope.host = appData.getHost();
	$scope.conditionAgree = false;
	$scope.iAgree = function(){
		$scope.conditionAgree = true;
	}
	$scope.iAgreeAndGoToTop = function() {
		$scope.iAgree();
	    document.body.scrollTop = 0;
	    document.documentElement.scrollTop = 0;
	}
	// This method is executed when the user press the "Sign in with Google" button
	  $scope.googleSignIn = function() {
	    $ionicLoading.show({
	      template: 'Logging in...'
	    });
	    window.plugins.googleplus.login(
	      {},
	      function (user_data) {
	        // For the purpose of this example I will store user data on local storage
	       /* UserService.setUser({
	          userID: user_data.userId,
	          name: user_data.displayName,
	          email: user_data.email,
	          picture: user_data.imageUrl,
	          accessToken: user_data.accessToken,
	          idToken: user_data.idToken
	        });*/
	    	  alert( user_data.email)
	        $ionicLoading.hide();
	    	  $state.transitionTo('menu.tab.home');
	      },
	      function (msg) {
	    	  alert( msg)
	        $ionicLoading.hide();
	      }
	    );
	  };
	
	
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
		if (window.localStorage.getItem('postlogin-moveto')){
			$state.transitionTo(window.localStorage.getItem('postlogin-moveto'));
			
		}else {
			$state.transitionTo('menu.tab.home');
		}
		
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