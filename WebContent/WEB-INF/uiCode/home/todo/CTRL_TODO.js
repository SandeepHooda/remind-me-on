APP.CONTROLLERS.controller ('CTRL_TODO',['$scope','$state','$rootScope','$ionicLoading','$http','$ionicPopup','appData',
    function($scope,$state,$rootScope,$ionicLoading,$http,$ionicPopup, appData){
	
	
	var theCtrl = this;
	theCtrl.newTodo = "";
	window.localStorage.setItem('postlogin-moveto','menu.tab.todo');
	var config = {
            headers : {
                'Content-Type': 'application/json;'
            }
        }
	$scope.todos = [];
	$scope.completedTodos = [];
	theCtrl.logOut = function(){
		$scope.$emit('logOut');
	}
	
	$scope.checkEnter = function(){
		if(event.keyCode == 13){
			$scope.addNewTodo();
		}
	}
	$scope.addNewTodo  = function(){
		if (!theCtrl.newTodo) return;
		var toDo = {};
		toDo.order = $scope.todos.length +1;
		toDo.taskDesc = theCtrl.newTodo;
		$scope.showBusy();
		$http.post(appData.getHost()+'/ws/todo/',toDo , config)
  		.then(function(response){
  			 $scope.hideBusy();
  			if (response.data){
  				
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
		
		 $http.get(appData.getHost()+'/ws/todo')
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			//$scope.todos = response.data ;
	  			$scope.showToDos(response.data);
	  			theCtrl.newTodo = "";
	  			
	  		},
			function(response){
	  			 $scope.hideBusy();
	  			$scope.popUp('Sorry ', 'Could not fectch data. Do you want to retry now?','menu.login' );
			});
		}
	$scope.showToDos = function(data){
		$scope.todos = []
		$scope.completedTodos = [];
		var todos = [];
		var completedTodos = [];
		for (var i=0;i<data.length;i++){
			if (data[i].complete){
				completedTodos.push(data[i]);
			}else {
				todos.push(data[i]);
			}
		}
		$scope.todos = todos;
		completedTodos.sort($scope.compareToDos)
		$scope.completedTodos = completedTodos;
	}
	
	$scope.toggleComplete = function(todo){
		 $scope.showBusy();
		 $http.delete(appData.getHost()+'/ws/todo/id/'+todo._id)
	  		.then(function(response){
	  			 $scope.hideBusy();
	  			$scope.showToDos(response.data);
	  		},
			function(response){
	  			 $scope.hideBusy();
				
			});
	}
	
	$scope.compareToDos = function(a, b) {
		  return  b.dateCompleted - a.dateCompleted;
	}
	$scope.compareToDosPending = function(a, b) {
		  return   a.order - b.order;
	}
	$scope.updateToDoOrderInDB = function(toDo){
		$http.post(appData.getHost()+'/ws/todo/update',toDo , config)
  		.then(function(response){
  			
  		},
		function(response){
  			});
	}
	$scope.moveDown = function(index){
		
		if (index < ($scope.todos.length -1)){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index+1].order;
			todos[index+1].order = order;
			$scope.todos = [];
			todos.sort($scope.compareToDosPending);
			$scope.todos = todos;
			$scope.updateToDoOrderInDB(todos[index]);
			$scope.updateToDoOrderInDB(todos[index+1]);
		}
		
	}
	$scope.moveUp = function(index){
		if (index >0){
			var todos = $scope.todos;
			var order = todos[index].order;
			todos[index].order = todos[index-1].order;
			todos[index-1].order = order;
			$scope.todos = [];
			todos.sort($scope.compareToDosPending);
			$scope.todos = todos;
			$scope.updateToDoOrderInDB(todos[index]);
			$scope.updateToDoOrderInDB(todos[index-1]);
		}
	}
	$scope.markeComplete = function(index){
		$scope.toggleComplete($scope.todos[index]);
	}
	$scope.markePending = function(index){
		$scope.toggleComplete($scope.completedTodos[index]);
	}
		
		
		$scope.popUp = function(subject, body, nextStep){
			var confirmPopup = $ionicPopup.confirm({
			     title: subject,
			     template: body
			   });
			 confirmPopup.then(function(res) {
				 if (res && nextStep){
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