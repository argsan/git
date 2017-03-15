var myOtherModule = angular.module("app", []);
(function() {
    //angular.module('app', ['angularPaho', 'paho']);
})();
myOtherModule.controller('railCtrl', function($rootScope, $scope, $http, $log, $location,$interval) {
  $scope.templates = [{url: 'views/homePage.html' },{ url: 'views/index1.html' },{ url: 'views/productDetails.html' }];
  
  $scope.template = $scope.templates[0];
  $scope.failureMsg = '';
  
  $scope.login = function () {
	  $scope.template = $scope.templates[1];
	  
  };
  $scope.productList = function(){
	  $scope.template = $scope.templates[2];
  }
  
 
  	
});
myOtherModule.controller('productCtrl', [ '$scope', 'MqttClient', '$timeout', function( $scope,MqttClient, $timeout) {
 
	var ip = "";
    var port = "";
    var id = "HelloWorldQoS1Consumer";
	var client = MqttClient.init(ip, port, id);
	
    MqttClient.connect({
    	userName:  "sample",
    	password:  "sample",
    	onSuccess: successCallback});

    function successCallback() {
		var message = new Paho.MQTT.Message("table=product");
		message.destinationName = "T/Booking/request";
		MqttClient.send(message);
		MqttClient.subscribe('T/Booking/resp');
		$timeout(getReurnValue, 2000);
    }
	
	function getReurnValue() {
		console.log("Response after 2 seconds : "+client._val);
		var val = client._val;
		if(val.error){
			alert("error")
		} else {
			$scope.products = (angular.fromJson(client._val)).records;
		}
	}	
 
}]);
(function() {
	myOtherModule.controller('test', [ '$scope', 'MqttClient', '$timeout', function($scope, MqttClient, $timeout) {

	
	var ip = "";
    var port = "";
    var id = "HelloWorldQoS1Consumer";
    var respon = "";

	$scope.getTrainList = function(){
		//alert("success");
	var client = MqttClient.init(ip, port, id);
	
    MqttClient.connect({
    	userName:  "sample",
    	password:  "sample",
    	onSuccess: successCallback});

    function successCallback() {
		var message = new Paho.MQTT.Message("table=trains");
		message.destinationName = "T/Booking/request";
		MqttClient.send(message);
		MqttClient.subscribe('T/Booking/resp');
		$timeout(getReurnValue, 2000);
    }
	
	function getReurnValue() {
		console.log("Response after 2 seconds : "+client._val);
		var val = client._val;
		if(val.error){
			alert("error")
		} else {
			$('#test1').hide();
			$('#sidebar').hide();
			$('#test2').show();
			$scope.names = (angular.fromJson(client._val)).records;
		}
	}	
	}
  
    
  }]);
})();
$(function() {
    $( "#datepicker-13" ).datepicker();
    //$( "#datepicker-13" ).datepicker("show");
 });
 
 
  	

myOtherModule.factory('MqttClient', [function() {
	
	// so we can use the member attributes inside our functions
	var client = {};
	// initialize attributes
	client._val = "";
	client._location = "";
	client._port = "";
	client._id = "";
	client._client = null;
	client._isConnected = false;

	// member functions
	client.init = init;
	client.connect = connect;
	client.disconnect = disconnect;
	client.send = send;
	client.startTrace = startTrace;
	client.stopTrace = stopTrace;
	client.subscribe = subscribe;
	client.unsubscribe = unsubscribe;

	return client;

	// onConnectionLost callback

	function _call(cb, args) {
		if (client._client) {
			cb.apply(this, args);
		} else {
			console.log('Angular-Paho: Client must be initialized first.  Call init() function.');
		}
	}

	function onConnectionLost(resp) {
		console.log("Angular-Paho: Connection lost on ", client._id, ", error code: ", resp);
		client._isConnected = false;
	}

	// connects to the MQTT Server
	function connect(options) {
		_call(_connect, [options]);
	}

	function _connect(options) {
		client._client.connect(options);
		client._isConnected = client._client.isConnected();
	}

	function disconnect() {
		_call(_disconnect);
	}

	function _disconnect() {
		client._client.disconnect();
		client._isConnected = false;
	}

	function init(location, port, id) {
		// initialize attributes
		client._location = location;
		client._port = port;
		client._id = id;
		console.log("init "+client._location+"  "+client._port+" "+client._id);
		// create the client and callbacks
		client._client = new Paho.MQTT.Client(client._location, Number(client._port), client._id)
		client._client.onConnectionLost = onConnectionLost;
		client._client.onMessageArrived = onMessageArrived;   
		return client;
	}

	function onMessageArrived(message) {
		client._val = message.payloadString;
	}
	
	function send(message) {
		_call(_send, [message]);
	}

	function _send(message) {
		client._client.send(message);
	}

	function startTrace() {
		_call(_startTrace);
	}

	function _startTrace() {
		client._client.startTrace();
	}

	function stopTrace() {
		_call(_stopTrace);
	}

	function _stopTrace() {
		client._client.stopTrace();
	}

	function subscribe(filter, options) {
		_call(_subscribe, [filter, options]);
	}

	function _subscribe(filter, options) {
		client._client.subscribe(filter, options);
	}

	function unsubscribe(filter, options) {
		_call(_unsubscribe, [filter, options]);
	}

	function _unsubscribe(filter, options) {
		client._client.unsubscribe(filter, options);
	}

}]);

