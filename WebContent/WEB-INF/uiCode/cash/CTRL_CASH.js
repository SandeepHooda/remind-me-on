APP.CONTROLLERS.controller ('CTRL_CASH',['$scope','$ionicLoading','$http','$ionicPopup','$state',
    function($scope,$ionicLoading,$http,$ionicPopup, $state){
	var theCtrl = this;
	$scope.recharge = {}
	$scope.recharge.amount = 100;
	$scope.paymentGateway = "Payment gateway"
	$scope.goToHome = function(){
		if (window.localStorage.getItem('postlogin-moveto')){
			$state.transitionTo(window.localStorage.getItem('postlogin-moveto'));
			
		}else {
			$state.transitionTo('menu.tab.home');
		}
	}
	$scope.validateAmount = function(){
		if (isNaN($scope.recharge.amount)){
			$scope.recharge.amount = 100;
		}
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