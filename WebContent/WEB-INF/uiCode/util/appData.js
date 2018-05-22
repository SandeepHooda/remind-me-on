APP.SERVICES.service ('appData',['$window','dataRestore','$ionicPopup',
    function( $window,dataRestore, $ionicPopup){
	this.cartItems = [];
	this.offerItems = [];
	this.getShopID = function () {
		return "1519981368108";
	}
	this.getHost = function () {
		var host = "https://remind-me-on.appspot.com";
		if ($window.location.host == ""){
			host = "phone";
			//host = "https://deliveratmydoor.appspot.com";
		}else if ($window.location.host.indexOf("localhost:8080") >=0 ){
			host = "";
		}
		
		return host;
	}
	
	this.showErrorMessage = function(httpCode){
		if ( httpCode == 403){
			var confirmPopup = $ionicPopup.confirm({
			     title: 'Password mimatch',
			     template: 'Your password donot match our records.'
			   });
			 confirmPopup.then(function(res) {
			  });
		}else {
			var confirmPopup = $ionicPopup.confirm({
			     title: 'Internal Server Error',
			     template: 'Something unusual happened at server.'
			   });
			 confirmPopup.then(function(res) {
			  });
				
		}
	}
	
	this.getOfferItems = function(){
		return this.offerItems;
	}
	this.setOfferItems = function(offerItems){
		this.offerItems = offerItems;
	}
	this.getCartItems = function(){
		return this.cartItems;
	}
	
	
}

]);