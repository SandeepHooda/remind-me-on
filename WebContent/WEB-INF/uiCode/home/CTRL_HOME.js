APP.CONTROLLERS.controller ('CTRL_HOME',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup',
    function($scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup){
	//https://github.com/apache/cordova-plugin-geolocation
	//cordova plugin add phonegap-nfc 
	//cordova plugin add cordova-plugin-vibration
	//cordova plugin add https://github.com/katzer/cordova-plugin-email-composer.git#0.8.2
	//cordova plugin add https://github.com/cowbell/cordova-plugin-geofence
	//cordova plugin add cordova-plugin-vibration
	//cordova plugin add cordova-plugin-device-motion
	//cordova plugin add cordova-plugin-whitelist
	//cordova plugin add cordova-plugin-shake
	//cordova plugin add cordova-plugin-sms
	//cordova plugin add cordova-plugin-android-permissions@0.6.0
	//cordova plugin add cordova-plugin-tts
	//cordova plugin add https://github.com/macdonst/SpeechRecognitionPlugin org.apache.cordova.speech.speechrecognition
	//cordova plugin add https://github.com/SandeepHooda/Speachrecognization org.apache.cordova.speech.speechrecognition
	//cordova plugin add https://github.com/katzer/cordova-plugin-background-mode.git
//	cordova plugin add cordova-plugin-http
	//cordova plugin add cordova-plugin-contacts-phonenumbers
	//cordova plugin add https://github.com/boltex/cordova-plugin-powermanagement
	//cordova plugin add https://github.com/katzer/cordova-plugin-local-notifications de.appplant.cordova.plugin.local-notification
	
	var theCtrl = this;
	theCtrl.searchInput = "";
	$scope.reminders =[];
	$scope.remindersInDB =[];
	var days = ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'];
	window.localStorage.setItem('postlogin-moveto','menu.tab.home');
	var name = window.localStorage.getItem('name');
	if (name ){
		$scope.userName = "Welcome "+name;
	}else {
		$scope.userName ="Hello Guest";
	}
	theCtrl.addNewReminder  = function(){
		$state.transitionTo('menu.newReminder');
	}
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	$scope.filterByText = function(){
		$scope.reminders =[];
		var filteredReminders = [];
		
		//Nothing to filter
		if (theCtrl.searchInput == ""){
			for (var i=0;i<$scope.remindersInDB.length;i++){
				filteredReminders.push($scope.remindersInDB[i]);
				
			}
			$scope.reminders = filteredReminders;
			  return;
		  }
		
		
		  var searchArray = theCtrl.searchInput.toLowerCase().split(" ");//split the input by space
		  
		  filteredReminders = 
				  	  _.filter($scope.remindersInDB, function(o) {//Search in all reminders in DB
				  		        var matchingProduct = true;
				  		      _.forEach(searchArray, function(search){ // look for all words in search string in any order and  case insensitive
				  		    	if ( (o.reminderSubject.toLowerCase().indexOf(search) < 0 ) && (o.reminderText.toLowerCase().indexOf(search) < 0 )){
				  		    		matchingProduct = false;
				  				}
				  		      });
						  		
				  		      if (matchingProduct) return o;
						  });
			 
		 
		  filteredReminders = _.without(filteredReminders, undefined);
		  filteredReminders = _.without(filteredReminders, null);
		  
		$scope.reminders = filteredReminders;
	}
	$scope.filter = function(frequencyType, frequencyWithDate){
		$scope.reminders =[];
		var filteredReminders = [];
		for (var i=0;i<$scope.remindersInDB.length;i++){
			if (frequencyType == null || ($scope.remindersInDB[i].frequencyType == frequencyType &&  $scope.remindersInDB[i].frequencyWithDate == frequencyWithDate)){
				filteredReminders.push($scope.remindersInDB[i]);
			}
		}
		$scope.reminders = filteredReminders;
	}
	$scope.gettingUserReminderList = false;
	$rootScope.$on('getMyRemindersList',function(event){
		if ($scope.gettingUserReminderList){
			return ;
		}
		$scope.showBusy();
		
		 $http.get('/ws/reminder')
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.formatReminderDisplay(response.data) ;
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
				
			});
		});
	$scope.formatReminderDisplay = function(userReminders){
		if (userReminders){
				for (var i=0;i<userReminders.length;i++){
					var date =  new Date(userReminders[i].nextExecutionTime);
					userReminders[i].nextExecutionDisplayTime =  days[ date.getDay() ] +" "+ date.toString("dd-MMM-yyyy HH:mm");  ;
				}
		  			$scope.reminders = userReminders;
		  			$scope.remindersInDB = userReminders;
			 }
	}
	$scope.deleteReminder = function(deleteIndex){
		$scope.deleteIndex  = deleteIndex;
		 var confirmPopup = $ionicPopup.confirm({
		     title: 'Confirmation',
		     template: 'Do you want to delete this reminder. Please note that delte a reminder means that all future ocurances will also be cancled.'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					 $http.delete('/ws/reminder/reminderID/'+$scope.reminders[$scope.deleteIndex]._id)
				  		.then(function(response){
				  			 $scope.hideBusy();
				  			$scope.formatReminderDisplay(response.data) ;
				  		},
						function(response){
				  			 $scope.hideBusy();
							
						});
				   
				   ///////////////////
				   for (var i=0; i <$scope.reminders.length;i++){
					   if (i==$scope.deleteIndex){
						 //splice is safe here as at a time only one item removed in whole iteration
						   $scope.reminders.splice(i,1);
						   //dataRestore.saveInCache('savedAddress', $scope.myData.savedAddress);
					   }
				   }
				  ////////////////////
			   }
		     
		   });
	}
	
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
		  $scope.$emit('getMyRemindersList');
	 
}])