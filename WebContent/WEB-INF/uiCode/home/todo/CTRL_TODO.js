APP.CONTROLLERS.controller ('CTRL_TODO',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup',
    function($scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup){
	
	
	var theCtrl = this;
	theCtrl.newTodo = "";
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.todos = [];
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	
	theCtrl.addNewTodo  = function(){
		if (!theCtrl.newTodo) return;
		var toDo = {};
		toDo.order = $scope.todos.length +1;
		toDo.taskDesc = theCtrl.newTodo;
		$scope.showBusy();
		$http.post('/ws/todo/',toDo , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				$scope.popUp('Success', 'Reminder added Successfully',null );
  				$scope.getToDos();
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
	$scope.getToDos = function(){
		
		$scope.showBusy();
		
		 $http.get('/ws/todo')
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.todos = response.data ;
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
				
			});
		}
		
		$scope.markComplete = function(deleteIndex){
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