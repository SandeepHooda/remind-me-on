APP.DIRECTIVE.directive("phoneNumber", [function() {
    return {
        restrict: "A",
        link: function(scope, elem, attrs) {
        	 var reg = new RegExp('[0-9,#()+ ]$');
            angular.element(elem).on("keyup", function(e) {
                if (!reg.test(this.value)) {
                	this.value = this.value.replace(/[^0-9,#()+ ]/g, "");
                }
            });
        }
    }
}]);