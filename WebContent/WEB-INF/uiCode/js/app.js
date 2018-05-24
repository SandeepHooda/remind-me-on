// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
var APP = {};
APP.DIRECTIVE = angular.module('allDirective',[]);
APP.CONTROLLERS = angular.module('allControllers',[]);
APP.SERVICES = angular.module('allServices',[]);
APP.FACTORY = angular.module('allFact',[]);
APP.DEPENDENCIES = ['allControllers',
                    'allServices',
                    'allDirective',
                    'allFact'
                    ];
APP.OTHERDEPENDENCIES = ['ionic','ngCordova','ionic-numberpicker'];
angular.module('starter', APP.DEPENDENCIES.concat(APP.OTHERDEPENDENCIES))
.config(['$urlRouterProvider','$stateProvider','$ionicConfigProvider',
         function($urlRouterProvider,$stateProvider,$ionicConfigProvider){
	$ionicConfigProvider.tabs.position('bottom');
	 // setup an abstract state for the tabs directive
				$stateProvider.state('menu',{
					url:'/menu',
					abstract: true,
					templateUrl:'menu.html'	
					 
					
				}).state('menu.contacts',{
					url:'/contacts',
					templateUrl: 'contacts/contacts.html',
					controller: 'CTRL_CONTACTS'
				}).state('menu.help',{
					url:'/help',
					templateUrl: 'help/help.html',
					controller: 'CTRL_help'
				}).state('menu.tab',{
					url:'/tab',
					abstract: true,
					templateUrl:'tabs.html'	
					 
					
				}).state('menu.tab.home',{
					url:'/home',
					views: {
						 'tab-home': {
						 templateUrl: 'home/home.html',
						 controller: 'CTRL_HOME'
						 }
					}	
					
				}).state('menu.snoozed',{
					url:'/snoozed',
					templateUrl: 'snoozed/snoozed.html',
					controller: 'CTRL_SNOOZED'
				}).state('menu.feedback',{
					url:'/feedback',
					templateUrl: 'help/feedback/feedback.html',
					controller: 'CTRL_feedback'
				}).state('menu.anexture',{
					url:'/anexture',
					templateUrl: 'help/annexure/annexure.html',
					controller: 'CTRL_help'
				}).state('menu.tab.todo',{
					url:'/todo',
					views: {
						 'tab-todo': {
						 templateUrl: 'home/todo/todo.html',
						 controller: 'CTRL_TODO'
						 }
					}	
					
				}).state('menu.newReminder',{
					url:'/newReminder',
					templateUrl: 'newReminder/newReminder.html',
					controller: 'CTRL_NewReminder'
				}).state('menu.login',{
					url:'/login',
					templateUrl: 'login/login.html',
					controller: 'CTRL_Login'
				}).state('menu.addcontacts',{
					url:'/addcontact',
					templateUrl: 'contacts/add/addcontacts.html',
					controller: 'CTRL_ADDCONTACTS'
				}).state('menu.addcash',{
					url:'/addcash',
					templateUrl: 'cash/cash.html',
					controller: 'CTRL_CASH'
				})
				$urlRouterProvider.otherwise('/menu/tab/home');
			}
         ])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
