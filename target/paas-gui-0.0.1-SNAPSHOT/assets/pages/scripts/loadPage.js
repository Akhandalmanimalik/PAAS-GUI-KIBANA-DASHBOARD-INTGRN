function loadpage(text){
	 
	if (text === 'activity'){
		$("#content").attr("src", "html/activity.html");
	};
	if (text === 'dashboardsummary'){
		$("#content").attr("src", "html/dashboardsummary.html");
	};
	if (text === 'dashboardresources'){
		alert("dashboardresources");
		(function() {
		    var startingTime = new Date().getTime();
		    // Load the script
		    var script = document.createElement("SCRIPT");
		    script.src = 'https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js';
		    script.type = 'text/javascript';
		    document.getElementsByTagName("head")[0].appendChild(script);

		    // Poll for jQuery to come into existance
		    var checkReady = function(callback) {
		        if (window.jQuery) {
		            callback(jQuery);
		        }
		        else {
		            window.setTimeout(function() { checkReady(callback); }, 20);
		        }
		    };

		    // Start polling...
		    checkReady(function($) {
		        $(function() {
		            var endingTime = new Date().getTime();
		            var tookTime = endingTime - startingTime;
		            window.alert("jQuery is loaded, after " + tookTime + " milliseconds!");
		        });
		    });
		})();
		var jqxhr = $.ajax({
			url: "/PAAS-GUI/rest/registerAndLoginService/test" 
			})
	    .success(function(data) { alert("success");
	    alert("data "+data);})
	    .error(function() { alert("error"); })
	    .complete(function() { alert("complete"); });

	

	// Set another completion function for the request above
	jqxhr.complete(function(){ alert("second complete"); });
		$("#content").attr("src", "html/dashboardresources.html");
	};
	
	/*this file should*/
	if (text === 'applicationsummary'){
		$("#content").attr("src", "html/applicantmain.html");
	};
	
	/*this file should remove it is for testig purpose*/
	/*if (text === 'applicationsummary'){
		$("#content").attr("src", "applicationsummary.html");
	};
	*/
	if (text === 'applicationwebhooks'){
		$("#content").attr("src", "html/applicationwebhooks.html");
	};
	if (text === 'environments'){
		$("#content").attr("src", "html/container.html");
	};
	if (text === 'vpc'){  
		$("#content").attr("src", "html/vpc.html");
	};
	
	if (text === 'subnet'){
		$("#content").attr("src", "html/subnet.html");
	};
	if (text === 'vpn'){
		$("#content").attr("src", "html/vpn.html");
	};
	if (text === 'acl'){
		
		$("#content").attr("src", "html/acl.html");
	};
	if (text === 'firewall'){
		$("#content").attr("src", "html/firewall.html");
	};
	if (text === 'certificates'){
		$("#content").attr("src", "html/certificates.html");
		
	};
	          /*policies*/
	if (text === 'scalingandrecovery'){
		$("#content").attr("src", "html/scalingandrecovery.html");
	};
	if (text === 'hostscaling'){
		$("#content").attr("src", "html/hostscaling.html");
	};
	if (text === 'serviceaffinities'){
		$("#content").attr("src", "html/serviceaffinities.html");
	};
	if (text === 'resourceSelection'){
		$("#content").attr("src", "html/resourceselection.html");
	};
	if (text === 'environmentTypes'){
		$("#content").attr("src", "html/environmenttypes.html");
	};
	if (text === 'containertype'){
		$("#content").attr("src", "html/containertype.html");
	};
	if (text === 'cloudprovider'){
		$("#content").attr("src", "html/cloudprovider.html");
	};
	if (text === 'storage'){
		$("#content").attr("src", "html/storage.html");
	};
	if (text === 'imageregistry'){
		$("#content").attr("src", "html/imagedocker.html");
	};
	if (text === 'logserver'){
		$("#content").attr("src", "html/logserver.html");
	};
	if (text === 'errordiagnosis'){
		$("#content").attr("src", "html/errordiagnosis.html");
	};
	if (text === 'allhost'){
		$("#content").attr("src", "html/allhost.html");
	};
	
}; 