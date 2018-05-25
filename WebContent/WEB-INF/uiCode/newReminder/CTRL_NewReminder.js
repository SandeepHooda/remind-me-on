APP.CONTROLLERS.controller ('CTRL_NewReminder',['$scope','$http','$rootScope','$ionicPopup','$state','$ionicLoading','$window','appData',
    function($scope, $http, $rootScope ,$ionicPopup,$state,$ionicLoading,$window,appData){

	var theCtrl = this;
	theCtrl.selectedPhone = "";
	$scope.reminder = {}
	 var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
var monthNames =[
		
		"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	]
	$scope.frequencyDays = ["Monday", "Tuesday", "Wednesday", "Thrusday","Friday", "Saturday", "Sunday"];
	$scope.frequencyDaysRepeat = ["Every", "First", "Second", "Third", "Fourth"];
	$scope.reminder.selectedDayRepeat = "Every";
	$scope.reminder.selectedDay = "Monday";
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
	$scope.moveToMinute_Day  =function(){
		if ($scope.reminder.hour > 23 || isNaN($scope.reminder.hour) ){
			$scope.reminder.hour = "";
		}
		if ($scope.reminder.hour.length ==2){
			var minute_Day = $window.document.getElementById('minute_Day');
			minute_Day.focus();
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
	
	$scope.addPhoneNoToProfile = function(){
		$state.transitionTo('menu.addcontacts');
	}
	$scope.toggleCall = function (){
		if($scope.verifiedPhones.length <= 0){
				$scope.reminder.makeACall = false;
				$scope.reminder.sendText = false;
		}else {
			$scope.reminder.makeACall = !$scope.reminder.makeACall;
		}
		
		window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
		window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
	}
	$scope.toggleSMS = function (){
		if($scope.verifiedPhones.length <= 0){
			$scope.reminder.makeACall = false;
			$scope.reminder.sendText = false;
		}else {
			$scope.reminder.sendText = !$scope.reminder.sendText;
		}
		window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
		window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
	}
	$scope.frequencyWithDate = "Once";
	$scope.changeFrequency = function(){
		if ($scope.frequencyWithDate == "Once"){
			$scope.frequencyWithDate = "Monthly";
		}else if ($scope.frequencyWithDate == "Monthly"){
			$scope.frequencyWithDate = "Yearly";
		}else {
			$scope.frequencyWithDate = "Once";
		}
	}
	$scope.frequencyType = "Date";
	$scope.changeFrequencyType = function(){
		if ($scope.frequencyType == "Date"){
			$scope.frequencyType = "Day";
		}else {
			$scope.frequencyType = "Date";
		}
	}
	$scope.validateRequiredfields = function(){
		var reminderObj = {};
		if (!$scope.reminder.reminderSubject || !$scope.reminder.reminderText){
			  return null;
		  }
		var allRequiredFields = true;
		if($scope.frequencyType =='Date'){
			 if ($scope.frequencyWithDate== "Once" ){
				 	if (!$scope.reminder.year  || !$scope.reminder.month || !$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute  ) {
				 		allRequiredFields = false;
				 	}else {
				 		reminderObj.displayTime = $scope.reminder.day + " " +monthNames[$scope.reminder.month-1] +" "+$scope.reminder.year +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute 
				 	}
			  }else  if ($scope.frequencyWithDate== "Monthly"){
				  if (!$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute) {
					  allRequiredFields = false;
				  }else {
					  reminderObj.displayTime = "Every month "+$scope.reminder.day +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
				  }
			  }else {
				  if (!$scope.reminder.month || !$scope.reminder.day || !$scope.reminder.hour || !$scope.reminder.minute) {
					  allRequiredFields = false;
				  }else {
					  reminderObj.displayTime = "Every Year  "+$scope.reminder.day + " " +monthNames[$scope.reminder.month-1] +" @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
				  }
			  }
			  
			  
			 
		}else {
			if (!$scope.reminder.hour || !$scope.reminder.minute) {
				  allRequiredFields = false;
			  }else {
				  reminderObj.displayTime = $scope.reminder.selectedDayRepeat +" "+$scope.reminder.selectedDay +" of every month @ "+ $scope.reminder.hour +" : "+$scope.reminder.minute;
			  }
			
		}
		if (allRequiredFields){
			return reminderObj;
		}else {
			return null;
		}
	}
	$scope.addReminder = function(){
		
		var reminderObj = $scope.validateRequiredfields();
		
		 
		  if(reminderObj == null){
				$scope.popUp('Invalid entry', 'Please fill all the mandatory fields',null );
				return;
			}
		  
		  
			reminderObj.regID = window.localStorage.getItem('regID');
			
			
			reminderObj.reminderSubject = $scope.reminder.reminderSubject ;
			reminderObj.reminderText = $scope.reminder.reminderText;
			
			reminderObj.makeACall = $scope.reminder.makeACall;
			reminderObj.sendText = $scope.reminder.sendText;
			
			reminderObj.frequencyType = $scope.frequencyType;
			reminderObj.selectedPhone = theCtrl.selectedPhone;
		if($scope.frequencyType =='Date'){
			reminderObj.frequencyWithDate = $scope.frequencyWithDate;//Once , Monthly, Yearly
			reminderObj.date =$scope.reminder.year+"_"+$scope.reminder.month+"_"+$scope.reminder.day;
			reminderObj.time = $scope.reminder.hour+"_"+$scope.reminder.minute;
			reminderObj._id = new Date($scope.reminder.year,$scope.reminder.month -1, $scope.reminder.day, $scope.reminder.hour, $scope.reminder.minute).getTime() +Math.random();
		
			
		}else {
			reminderObj.time = $scope.reminder.hour+"_"+$scope.reminder.minute;
			reminderObj._id = new Date().getTime()
			reminderObj.dayRepeatFrequency = $scope.reminder.selectedDayRepeat +" "+$scope.reminder.selectedDay ;
			
		}
		
			
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/reminder/',reminderObj , config)
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
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
		});
	}
	$scope.verifiedPhones  = [];
	$scope.setCallAndTextSettings = function(){
		$scope.reminder.makeACall = true;
		$scope.reminder.sendText = true;
		if (window.localStorage.getItem('makeACall') && window.localStorage.getItem('makeACall') == "false"){
			$scope.reminder.makeACall = false;
		}
		if (window.localStorage.getItem('sendText') && window.localStorage.getItem('sendText') == "false"){
			$scope.reminder.sendText = false;
		}
	}
	$scope.getVerifiedPhones = function(){
		$http.get(appData.getHost()+'/ws/phone/verified/true')
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifiedPhones = response.data;
  				if($scope.verifiedPhones.length > 0){
  					theCtrl.selectedPhone = $scope.verifiedPhones[0];
  					
  					$scope.setCallAndTextSettings();
  				}else {
  					$scope.reminder.makeACall = false;
  					$scope.reminder.sendText = false;
  					window.localStorage.setItem('makeACall', ""+$scope.reminder.makeACall);
  					window.localStorage.setItem('sendText', ""+$scope.reminder.sendText);
  					
  				}
  			}else {
  				$scope.popUp('Failure', 'Please retry',null )
  			}
  			
  		},
		function(response){
  			 $scope.hideBusy();
  			
  			 if (response.status == 401) {
  				$scope.popUp('Failure', 'Please login back and then retry.',null );
  			 }else {
  				$scope.popUp('Failure', 'Please retry.',null );
  			 }
  			
  			
			
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
		  
		  $scope.getVerifiedPhones();
	 
}])