APP.CONTROLLERS.controller ('CTRL_CONTACTS',['$scope','$state',
    function($scope,$state){
	var theCtrl = this;
	$scope.newContact = function (){
		$state.transitionTo('menu.addcontacts');
	}
}])