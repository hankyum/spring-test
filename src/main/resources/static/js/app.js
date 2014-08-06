var app = angular.module("myApp", [ 'ngRoute' ]);
app.controller("PersonList", [ "$scope", "$http", function($scope, $http) {
	$http({
		method : "GET",
		url : "/persons"
	}).success(function(data) {
		$scope.persons = data;
	});
} ]);
app.controller("NavController", [ '$scope', '$http', function($scope, $http) {
	$scope.messages = [ {
		from : "Hank",
		time : "xxx",
		message : "Message short"
	}, {
		from : "John Smith",
		time : "Yesterday at 4:32 PM",
		message : "Lorem ipsum dolor sit amet, consectetur..."
	} ];
} ]);
app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : '/home.html'
	}).when('/forms', {
		templateUrl : '/forms.html'
	}).when('/charts', {
		templateUrl : '/charts.html'
	}).when('/tables', {
		templateUrl : '/tables.html'
	}).when('/bootstrap-elements', {
		templateUrl : '/bootstrap-elements.html'
	}).when('/bootstrap-grid', {
		templateUrl : '/bootstrap-grid.html'
	}).when('/personList', {
		templateUrl : '/personList.html',
		controller : 'PersonList'
	}).otherwise({
		redirectTo : '/home'
	});
} ]);