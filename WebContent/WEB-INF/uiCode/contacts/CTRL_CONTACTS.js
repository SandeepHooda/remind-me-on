APP.CONTROLLERS.controller ('CTRL_CONTACTS',['$scope','$ionicLoading','$http','$ionicPopup','$state','appData',
    function($scope,$ionicLoading,$http,$ionicPopup, $state,appData){
	var theCtrl = this;
	$scope.data = {};
	$scope.data.otpValue = "";
	$scope.userPhonesVerified = [];
	$scope.userPhonesNotVerified = [];
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.newContact = function (){
		$state.transitionTo('menu.addcontacts');
	}
	
	$scope.getContacts = function(){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/')
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.populatePhoneNos(response);
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
	
	$scope.populatePhoneNos = function(response){
		$scope.userPhonesVerified = [];
		$scope.userPhonesNotVerified = [];
		for (var i =0; i<response.data.length;i++){
				response.data[i].number = "( "+response.data[i].number.substring(0,3) +" ) "+response.data[i].number.substring(3,6) +" - "+response.data[i].number.substring(6);
				if (response.data[i].verified){
					$scope.userPhonesVerified.push(response.data[i]);
				}else {
					$scope.userPhonesNotVerified.push(response.data[i]);
				}
			}
			
	}
	$scope.deleteContacts = function(index){
		
		
		 var confirmPopup = $ionicPopup.confirm({
		     title: 'Confirmation',
		     template: 'Do you want to delete this Phone no?'
		   });

		   confirmPopup.then(function(res) {
			   if (res){
				   
				   $scope.showBusy();
					
					$http.delete(appData.getHost()+'/ws/phone/phoneID/'+$scope.userPhonesNotVerified[index]._id)
			  		.then(function(response){
			  			 $scope.hideBusy();
			  			if (response.data){
			  				$scope.populatePhoneNos(response);
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
		   });
		
	}
	
	$scope.confirmOtp = function(index){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/phoneID/'+$scope.userPhonesNotVerified[index]._id+'/confirmOtp/'+$scope.data.otpValue)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifyOptInpt = -1;
  				$scope.data.otpValue = "";
  				$scope.getContacts();
  				
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
	$scope.verifyOptInpt = -1;
	$scope.verify = function(index){
		$scope.showBusy();
		
		$http.get(appData.getHost()+'/ws/phone/verify/ID/'+$scope.userPhonesNotVerified[index]._id)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.verifyOptInpt = index;
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
		  
		  $scope.getContacts();
}])