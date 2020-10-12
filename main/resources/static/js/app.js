/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */

var app = angular.module("app", ['ngFileUpload']);

app.controller('xbrlController', ['$http','$scope', 'Upload', '$timeout', function ($http, $scope, Upload, $timeout) {

	$scope.init = function(){
		//$scope.host = 'https://xbrlframework.herokuapp.com';
		$scope.host = 'http://localhost:8080';
		$scope.user_url = '';
		$scope.sss = '';
		$scope.loadStatus = '	';
		$scope.msg = 'select a file or type a valid URL';
		$scope.f = '{\n "report" : {\n   \n } \n}';
		$scope.filename = '[waiting XML instance]';

	}

	$scope.init();	

	$scope.uploadFiles = function(file) {


		if (file != null){		

			$scope.filename = file.name;

			if (file.name.includes(".xml") || file.name.includes(".xbrl") ) {

				if (file.name.includes("cal") || file.name.includes("lab") || file.name.includes("pre") || file.name.includes("ref") || file.name.includes("def")) {

					$scope.f = '{\n "msg": "xbrl-json-CR-2017-05-02 has just specified XBRL instances in Json format. For this reason, this tool just converts XBRL instances." \n}';

				}else{

					if (file.size <= 15000000) {

						if ($scope.key && $scope.key.includes("@") && $scope.key.includes(".")){

							var file = file;

							//preload
							file.upload = Upload.upload({
								url: $scope.host+'/preload-file',
								data: {file: file}
							});

							file.upload.then(
									function success (response) {
										$scope.f = JSON.stringify(response.data, undefined, 4);
										$scope.msg = response.data.report.fact[0].msg;
										$timeout(function () {console.log(response.status);});
									}, 
									function unsuccess (response) {
										//console.log("response is a error: "+response);
										$scope.f = '{ "There were problems in loading your file. Possibilities: (1)  Make sure it is following the rules of XML formatting (https://www.w3.org/TR/xml/#sec-well-formed)." }' ;
									});


							//upload
							file.upload = Upload.upload({
								url: $scope.host+'/upload-file',
								data: {file: file, email:$scope.key}
							});      

							file.upload.then(
									function success (response) {
										$scope.sss = 'Below, you have it in XBRL-JSON format:';
										$scope.f = JSON.stringify(response.data, undefined, 4);
										$scope.msg = 'An email has been sent to '+$scope.key+' with the XBRL-JSON file. Please, also check your SPAM box.';
										//$scope.f = "An email has been sent to "+$scope.key+" with the XBRL-JSON file. Please, also check your SPAM box.";
										//alert("An email has been sent to "+$scope.key+", subject \"[XBRL-JSON] ...\". Please, also check the SPAM box.")
										$timeout(function () {console.log(response.status);});
									}, 
									function unsuccess (response) {
										//console.log("response is a error: "+response);
										$scope.f = '{ "There were problems in loading your file. Possibilities: (1) Make sure it is following the rules of XML formatting (https://www.w3.org/TR/xml/#sec-well-formed)." }' ;
										$scope.msg = "something went wrong on server."
									});		
						}else{
							console.log("no valid email");
							$scope.f = '{\n  "msg": "Please, it\'s needed a valid email for sending the XBRL-JSON file" \n}';
						}

					}else{
						console.log("For while, max size per file is 15mb.");
						$scope.f = '{\n  "msg": "Sorry! For while, max size per file is 15mb" \n}';
					}

				}
			}else{ // if is not xml or xbrl file
				console.log("file must be in XML or XBRL format");
				$scope.f = '{\n "msg": "Sorry! This file must be in XML or XBRL format" \n}';
			}
		} // if not null

	}



	$scope.sendUrl = function() {

		if ($scope.user_url.includes("http://") || $scope.user_url.includes("https://")){

			if ($scope.user_url.includes(".xml") || $scope.user_url.includes(".xbrl")){

				var temp = $scope.user_url.split("/");
				$scope.filename = temp[temp.length-1];

				if ($scope.filename.includes("cal") || $scope.filename.includes("lab") || $scope.filename.includes("pre") || $scope.filename.includes("ref") || $scope.filename.includes("def")) {

					$scope.f = '{\n "msg": "xbrl-json-CR-2017-05-02 has just specified XBRL instances in Json format. For this reason, this tool just converts XBRL instances." \n}';

				}else{


					if ($scope.key && $scope.key.includes("@") && $scope.key.includes(".")){	

						//preload
						$http({
							method:'POST', 
							url: $scope.host+'/preload-uri',
							data: {uri: $scope.user_url}
						})
						.then(function success(response){
							$scope.f = JSON.stringify(response.data, undefined, 4);
							$scope.msg = response.data.report.fact[0].msg;
							$timeout(function () {console.log(response.status);});
						},function unsuccess(response){
							//console.log("response is a error: "+response);
							$scope.f = '{ "There were problems in loading your file. Possibilities: (1) Make sure it is following the rules of XML formatting (https://www.w3.org/TR/xml/#sec-well-formed)." }' ;
							$scope.msg = "something went wrong on server."
						});


						//upload
						$http({
							method:'POST', 
							url: $scope.host+'/upload-uri',
							data: {uri: $scope.user_url, email:$scope.key}
						})
						.then(function success(response){
							$scope.sss = 'Below, you have it in XBRL-JSON format:';
							$scope.f = JSON.stringify(response.data, undefined, 4);
							$scope.msg = 'An email has been sent to '+$scope.key+' with the XBRL-JSON file. Please, also check your SPAM box.'; 
							$timeout(function () {console.log(response.status);});
						},function unsuccess(response){
							//console.log("response is a error: "+response);
							$scope.f = '{ "There were problems in loading your file. Possibilities: (1) Make sure it is following the rules of XML formatting (https://www.w3.org/TR/xml/#sec-well-formed)." }' ;
							$scope.msg = "something went wrong on server."
						});


					}else{
						console.log("no valid email");
						$scope.f = '{\n  "msg": "Please, it\'s needed a valid email for sending the XBRL-JSON file" \n}';
					}
				}

			}else{
				$scope.f = '{\n "msg": "This URL must contain a XML or XBRL file", '+
				'\n  "example": "https://www.sec.gov/Archives/edgar/data/1169567/000116956714000019/oxfo-20140930.xml" \n}';
			}

		}else{
			$scope.f = '{\n  "msg": "Your URL is not valid/wellformed", '+
			'\n  "[1]": "There is no "http://" or "https://", ' +
			'\n  "example": "https://www.sec.gov/Archives/edgar/data/1169567/000116956714000019/oxfo-20140930.xml" \n}';
		}
	}

}]);
