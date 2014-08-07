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
app.controller("CreatePerson", [
		'$scope',
		'$http',
		function($scope, $http, transformRequestAsFormPost) {
			$scope.data = {};
			$scope.submiting = false;
//			$scope.processingForm = function() {
//				if (!$scope.submiting) {
//					$scope.submiting = true;
//					 $.ajax({
//						 type : 'POST',
//						 url : '/person1',
//						 data : $scope.data, // pass in data as strings
//						 dataType : 'json',
//						 headers : {
//						 'Content-Type' : 'application/x-www-form-urlencoded'
//						 },
//						 success : function(data) {
//							 alert(data);
//						 },
//						 error : function(data) {
//							 alert("Error");
//						 }
//					 });
//				}
//				return false;
//			};

			/*
			 * $scope.processingForm = function() { $scope.submitting = true;
			 * alert( $scope.data.firstName); $http.post("/person1",
			 * $scope.data).success(function(data) { $scope.submitting = false;
			 * $modalInstance.close(data); }).error(function(data, status) {
			 * $scope.submitting = false; if (status === 400) {
			 * $scope.badRequest = data; } else if (status === 409) {
			 * $scope.badRequest = 'The name is already used.'; } }); };
			 */
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
	}).when('/createPerson', {
		templateUrl : '/person1',
		controller : 'CreatePerson'
	}).when('/personList', {
		templateUrl : '/personList.html',
		controller : 'PersonList'
	}).otherwise({
		redirectTo : '/home'
	});
} ]);