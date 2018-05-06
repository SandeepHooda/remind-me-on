APP.CONTROLLERS.controller ('CTRL_NewReminder',['$scope','$http','$rootScope','$ionicPopup','$state','$ionicLoading','$window',
    function($scope, $http, $rootScope ,$ionicPopup,$state,$ionicLoading,$window){

	var theCtrl = this;
	$scope.reminder = {}
	 var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	
	

	$scope.moveToMonth =function(){
		if ( ($scope.reminder.year > 999 && $scope.reminder.year < 2018) || isNaN($scope.reminder.year) ){
			$scope.reminder.year = "";
		}
		if ($scope.reminder.year.length == 4){
			var month = $window.document.getElementById('month');
			month.focus();
		}
		
	}
	$scope.moveToDay =function(){
		if ($scope.reminder.month > 12 || isNaN($scope.reminder.month) ){
			$scope.reminder.month = "";
		}
		if ($scope.reminder.month.length == 2){
			var day = $window.document.getElementById('day');
			day.focus();
		}
		
	}
	$scope.moveToHour =function(){
		if ($scope.reminder.day > 31 || isNaN($scope.reminder.day) ){
			$scope.reminder.day = "";
		}
		if ($scope.reminder.day.length == 2){
			var hour = $window.document.getElementById('hour');
			hour.focus();
		}
		
	}
	$scope.moveToMinute =function(){
		if ($scope.reminder.hour > 23 || isNaN($scope.reminder.hour) ){
			$scope.reminder.hour = "";
		}
		if ($scope.reminder.hour.length ==2){
			var minute = $window.document.getElementById('minute');
			minute.focus();
		}
		
	}
	$scope.moveToSubject = function(){
		if ($scope.reminder.minute > 59 || isNaN($scope.reminder.minute) ){
			$scope.reminder.minute = "";
		}
		if ($scope.reminder.minute.length ==2){
			var subject = $window.document.getElementById('subject');
			subject.focus();
		}
		
	}
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			//$scope.addReminder();
		}
	}
	$scope.reminder.makeACall = true;
	$scope.reminder.sendText = true;
	$scope.toggleCall = function (){
		$scope.reminder.makeACall = !$scope.reminder.makeACall;
	}
	$scope.toggleSMS = function (){
		$scope.reminder.sendText = !$scope.reminder.sendText;
	}
	$scope.addReminder = function(){
		var reminderObj = {};
		reminderObj.regID = window.localStorage.getItem('regID');
		reminderObj.date =$scope.reminder.year+"_"+$scope.reminder.month+"_"+$scope.reminder.day;
		reminderObj.time = $scope.reminder.hour+"_"+$scope.reminder.minute;
		reminderObj.reminderSubject = $scope.reminder.reminderSubject ;
		reminderObj.reminderText = $scope.reminder.reminderText;
		if(!$scope.reminder.year || !$scope.reminder.month || !$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute ||
				!$scope.reminder.reminderSubject || !$scope.reminder.reminderText ){
			$scope.popUp('Invalid entry', 'Please fill all the mandatory fields',null );
			return;
		}
			
		$scope.showBusy();
		$http.post('/ws/reminder/',reminderObj , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.popUp('Success', 'Reminder added Successfully','menu.tab.home' );
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			$scope.popUp('Failure', 'Please retry',null );
			
		});
	}
	
	$scope.popUp = function(subject, body, nextStep){
		var confirmPopup = $ionicPopup.confirm({
		     title: subject,
		     template: body
		   });
		 confirmPopup.then(function(res) {
			 if (nextStep){
				 $state.transitionTo(nextStep);
			 }
		  });
	}
	
	//Busy icon
	  $scope.showBusy = function() {
		    $ionicLoading.show({
		      template: 'Please Wait...',
		      duration: 10000
		    }).then(function(){
		       console.log("The loading indicator is now displayed");
		    });
		  };
		  $scope.hideBusy = function(){
		    $ionicLoading.hide().then(function(){
		       console.log("The loading indicator is now hidden");
		    });
		  };
	 
}])