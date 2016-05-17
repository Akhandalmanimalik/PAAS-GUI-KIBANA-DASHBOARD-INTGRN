var myenvironmenttype = angular.module('myenvironmenttype', []);

myenvironmenttype.controller('MainCtrl', function ($scope,$http) {
	
	$scope.field = {};
	
	 $scope.showModal = false;
	    $scope.toggleModal = function(){
	        $scope.showModal = !$scope.showModal;
	    };
   
    
    
 /*============ ENVIRONMENT TYPES REG=============*/ // DONE
    
    $scope.regEnvironmentTypes = function() {
    	console.log($scope.field);
    	var userData = JSON.stringify($scope.field);
    	var res = $http.post('/PAAS-GUI/rest/environmentTypeService/insertEnvironmentType', userData);
    	console.log(userData);

    	res.success(function(data, status, headers, config) {
    		$scope.message = data;
    		//document.location.href = '/paas-gui/html/login.html';
    	});
    	res.error(function(data, status, headers, config) {
    		alert("failure message: " + JSON.stringify({
    			data : data
    		}));
    	});
    }; 
    
    
    /*==================POPULATE DATA TO TABLE===================*/ // DONE
    
 	 $scope.selectEnvironmentTypes = function() {
 		 //console.log("hiii");
    	var response = $http.get('/PAAS-GUI/rest/environmentTypeService/getAllEnvironmentType');
    	response.success(function(data){
    		$scope.fields = data;
    		console.log("data given");
    	});
    	response.error(function(data, status, headers, config) {
                alert("Error in Fetching Data");
        });
    };   
    
/*    $scope.selectEnvironmentTypes = function() {
		 //console.log("hiii");
   	var response = $http.get('/paas-gui/rest/fetchData/displaygateway');
   	response.success(function(data){
   		$scope.display = data;
   		console.log("data given");
   	});
   	response.error(function(data, status, headers, config) {
               alert("Error in Fetching Data");
       });
   };   */                                          //Done
    $scope.selectEnvirnamentList = function() {
		 //console.log("hiii");
   	var response = $http.get('/PAAS-GUI/rest/environmentTypeService/getAllEnvironamentList');
   	response.success(function(data){
   		$scope.envirnamentlist = data;
   		
   	});
   	response.error(function(data, status, headers, config) {
               alert("Error in Fetching Data");
       });
   }; 

    /*=================== delete*====================*/ // DONE
   
    $scope.deleteEnvironmentTypes = function(data) {
     	var response = $http.get('/PAAS-GUI/rest/environmentTypeService/deleteEnvironmentByName/'+data);
     	response.success(function(data){
     		$scope.select();
     	});
     	response.error(function(data, status, headers, config) {
                 alert("Error in Fetching Data");
         });
     	
     };
    
     //Done
    
     $scope.getApplicationName = function(data) {
      	var response = $http.get('/PAAS-GUI/rest/environmentTypeService/getApplicationSummary');
      	response.success(function(data){
      		$scope.applicationlist=data;
      		
      		console.log("-------$scope.applicationlist----"+$scope.applicationlist);
      	});
      	response.error(function(data, status, headers, config) {
                  alert("Error in Fetching Data");
          });
      	
      };
    
      $scope.selectapp = function(reponame) {
 		// alert("selectSummary  function");
 		$scope.reponames;
 //JSON.stringify(data);
 		
 		$scope.isImg=true;
 	var response = $http.post('/PAAS-GUI/rest/environmentTypeService/getImageRepositoryFromSummary',reponame);   //to do
 	
 	response.success(function(data){
 		$scope.isImg=false;
 		$scope.reponames = data;
 		console.log("selectRepoName >>>> "+$scope.reponames);
 	});
 	response.error(function(data, status, headers, config) {
             alert("Error in Fetching Data");
     });
 };
 
 $scope.storeEnvironments = function() {   //Done
    	console.log($scope.field);
    	var userData = JSON.stringify($scope.field);
    	var res = $http.post('/PAAS-GUI/rest/environmentTypeService/insertEnvironmentsData', userData);
    	console.log(userData);

    	res.success(function(data, status, headers, config) {
    		$scope.message = data;
    		$scope.selectEnvirnamentList();
    		
    		//document.location.href = '/paas-gui/html/login.html';
    	});
    	res.error(function(data, status, headers, config) {
    		alert("failure message: " + JSON.stringify({
    			data : data
    		}));
    	});
    }; 
 
 $scope.selectappfortag = function(reponame) {
	 $scope.field.tag;
	 $scope.env;
		// alert("selectSummary  function");
		//alert("reponame : "+reponame);

		 angular.forEach($scope.services,function(item){
			 
			// alert("item: "+item.serviceName)
			 
			   if(item.serviceName == reponame){
				  
				   //alert("coming inside this ");
				   $scope.field.tag = item.tag;
				   console.log("tag"+$scope.field.tag);
				   $scope.env=item.env;
				  // alert("==="+$scope.env);
				   // alert("====="+angular.Json(item.env));
 }
});
	
	/*response.success(function(data){
		alert("data "+data);
		$scope.tagdata=data;
		alert("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj "+$scope.tagdata);
	
		
	});*/
	/*response.error(function(data, status, headers, config) {
          alert("Error in Fetching Data");
  });*/
};
    
 $scope.selectServices = function() {  // Done
   	var response = $http.get('/PAAS-GUI/rest/applicationService/getAllApplicationService');
   	response.success(function(data){
   		
   		$scope.services = data;
   		console.log("jjjjjjjj "+$scope.services);
   	});
   	response.error(function(data, status, headers, config) {
               alert("Error in Fetching Data");
       });
   };
 

});      /*===============end of controllers=======================*/

/*POPULATE DATA TO TABLE*/

	



/*=============directive starts================*/

myenvironmenttype.directive('modal', function () {
	return {
		template : '<div class="modal fade">'
				+ '<div class="modal-dialog">'
				+ '<div class="modal-content">'
				+ '<div class="modal-header">'
				+ '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
				+ '<h4 class="modal-title">{{ title }}</h4>'
				+ '</div>'
				+ '<div class="modal-body" ng-transclude></div>'
				+ '</div>' + '</div>' + '</div>',
		restrict : 'E',
		transclude : true,
		replace : true,
		scope : true,
		link : function postLink(scope, element, attrs) {
			scope.title = attrs.title;

			scope.$watch(attrs.visible, function(value) {
				if (value == true)
					$(element).modal('show');
				else
					$(element).modal('hide');
			});

			$(element).on('shown.bs.modal', function() {
				scope.$apply(function() {
					scope.$parent[attrs.visible] = true;
				});
			});

			$(element).on('hidden.bs.modal', function() {
				scope.$apply(function() {
					scope.$parent[attrs.visible] = false;
				});
			});
			
			$('.continue').click(function() {
				$('.nav-tabs > .active').next('li').find('a').trigger('click');
			});
			$('.back').click(function() {
				$('.nav-tabs > .active').prev('li').find('a').trigger('click');
			});
			$('.cancel').click(function() {
				$('.nav-tabs > .active').cancel('li').find('a').trigger('click');
			});
			
		}
	};
});