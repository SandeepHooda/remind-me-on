APP.CONTROLLERS.controller ('CTRL_feedback',['$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($scope,$ionicLoading,$http,$ionicPopup, $state,appData){
	var theCtrl = this;
	$scope.feedback = {};
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.sendFeedback = function(){
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/feedback/',$scope.feedback.text , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response){
  				$scope.popUp('Thanks', 'We have received your feedback and will work on that soon.',null );
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
}])