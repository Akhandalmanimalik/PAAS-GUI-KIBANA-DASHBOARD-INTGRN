var myvpc = angular.module('VpcApp', []);

myvpc.controller('VpcCtrl', function ($scope,$http) {
	
	$scope.field = {};
	
    $scope.showModal = false;
    $scope.toggleModal = function(){
        $scope.showModal = !$scope.showModal;
    };
    
      /*================VPC REGISTRATION===================*/
    
    $scope.storeApplicantUser = function() {
  	  console.log($scope.field);
  	  var userData = JSON.stringify($scope.field);
  	  var res = $http.post('/paas-gui/rest/fetchData/storeApplicantUser', userData);
  	  console.log(userData);
  	  res.success(function(data, status, headers, config) {
  	    $scope.message = data;
  	   
  	    window.location = "html/applicantmain.html";
  	 
  	  });
  	  res.error(function(data, status, headers, config) {
  	    alert("failure message: " + JSON.stringify({
  	      data : data
  	    }));
  	  });
  	 
  	};
  	
  	           /*POPULATE DATA TO TABLE*/
  
  	 $scope.selectApplicantName = function() {
     	var response = $http.get('/paas-gui/rest/fetchData/selectApplicantName');
     	response.success(function(data){
     		$scope.fields = data;
     		 
     	});
     	response.error(function(data, status, headers, config) {
                 alert("Error in Fetching Data");
         });
     };
     
  	         /*DELETE POPULATED DATA*/

     $scope.deleteData = function(data) {
    	 alert("comming  deleteData");
     	var response = $http.get('/paas-gui/rest/fetchData/deleteData/'+data);
     	response.success(function(data){
     		$scope.select();
     	});
     	response.error(function(data, status, headers, config) {
                 alert("Error in Fetching Data");
         });
     	
     };
     
              /*EDIT VPC DATA*/
     $scope.update = function(data) {
     	var response = $http.get('/paas-gui/rest/fetchData/updata/'+data);
     	response.success(function(data){
     		$scope.select();
     	});
     	response.error(function(data, status, headers, config) {
                 alert("Error in Fetching Data");
         });
     };
     
     
    
  	
  	

    
});  /*end of controller*/


myvpc.directive('modal', function () {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog">' + 
            '<div class="modal-content">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title">{{ title }}</h4>' + 
              '</div>' + 
              '<div class="modal-body" ng-transclude></div>' + 
            '</div>' + 
          '</div>' + 
        '</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
        scope.title = attrs.title;

        scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
  });
