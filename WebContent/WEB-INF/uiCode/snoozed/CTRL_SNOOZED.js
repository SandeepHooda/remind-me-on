APP.CONTROLLERS.controller ('CTRL_SNOOZED',['$scope','$ionicSideMenuDelegate','$state','$http','$rootScope','$ionicPopup','$ionicLoading','appData',
    function($scope, $ionicSideMenuDelegate,$state,$http,$rootScope,$ionicPopup,$ionicLoading,appData){
	var theCtrl = this;
	var regID = window.localStorage.getItem('regID');
	$scope.snoozedReminders = [];
	$scope.currentCallCredits = 0;
	if (document.URL.indexOf('localhost')>=0){
		regID = "070dd753-08e7-4a58-81f9-29ea3ad6c6c0";
		 window.localStorage.setItem('regID', regID);
	}
	
	if (!regID){
		$state.transitionTo('menu.login');
	}else {
		 $http.get(appData.getHost()+'/ws/login/validate/'+regID+'/timeZone/'+Intl.DateTimeFormat().resolvedOptions().timeZone.replace("/", "@"))
	  		.then(function(response){
	  			$scope.getCallCredits();
	  			$scope.recordLoginSucess();
	  		},
			function(response){
	  			window.localStorage.setItem('regID', 'invalid');
  				localStorage.removeItem('name');
  				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				$state.transitionTo('menu.login');
				
			});
	}
	
	$scope.getSnoozedReminders = function(){
		$http.get(appData.getHost()+'/ws/snoozed/reminder')
  		.then(function(response){
  			$scope.snoozedReminders = response.data;
  		
  		},
		function(response){
  			window.localStorage.setItem('regID', 'invalid');
				localStorage.removeItem('name');
				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			$state.transitionTo('menu.login');
			
		});
	}
	$scope.deleteReminder = function(deleteIndex){
		$scope.deleteIndex  = deleteIndex;
		 var confirmPopup = $ionicPopup.confirm({
		     title: ''+$scope.snoozedReminders[$scope.deleteIndex].reminderSubject+" "+$scope.snoozedReminders[$scope.deleteIndex].reminderText,
		     template: 'Do you want to delete this reminder.'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					 $http.delete(appData.getHost()+'/ws/snoozed/reminder/'+$scope.snoozedReminders[$scope.deleteIndex]._id)
				  		.then(function(response){
				  			 $scope.hideBusy();
				  			$scope.snoozedReminders = response.data;
				  		},
						function(response){
				  			 $scope.hideBusy();
							
						});
				   
				  
			   }
		     
		   });
	}
	
	$scope.getCallCredits = function(){
		$http.get(appData.getHost()+'/ws/callcredits/regid/'+regID)
  		.then(function(response){
  			$scope.currentCallCredits = response.data;
  			$scope.$emit('getMyRemindersList');
  		},
		function(response){
  			window.localStorage.setItem('regID', 'invalid');
				localStorage.removeItem('name');
				document.cookie = 'regID' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
				document.cookie = 'name' + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
			$state.transitionTo('menu.login');
			
		});
	}
	
	$scope.recordLoginSucess = function(){
		$http.get(appData.getHost()+'/ws/recordLoginSucess')
  		.then(function(response){
  			
  		},
		function(response){
  			
			
		});
	}
	
	$rootScope.$on('logOut',function(event){
		 regIDStorege = window.localStorage.getItem('regID');
		 $http.get(appData.getHost()+'/ws/logout/'+regIDStorege)
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
	
	$scope.addCash = function(){
		$state.transitionTo('menu.addcash');
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
	  
	//Busy icon
	  $scope.showBusy = function() {
		  $scope.gettingUserReminderList = true;
		    $ionicLoading.show({
		      template: 'Please Wait...',
		      duration: 10000
		    }).then(function(){
		       console.log("The loading indicator is now displayed");
		    });
		  };
		  $scope.hideBusy = function(){
			  $scope.gettingUserReminderList = false;
		    $ionicLoading.hide().then(function(){
		       console.log("The loading indicator is now hidden");
		    });
		  };
	  
	  
}
])