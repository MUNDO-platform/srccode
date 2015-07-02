var wfsApp = angular.module('WfsApp', ['ui.bootstrap']);

wfsApp.controller('WfsCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.wfses = [];
    $scope.wfs = {};
    $scope.help = {};    
    $scope.alerts = []; 
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.wfs.id = '';
        $scope.wfs.name = '';
        $scope.wfs.description = '';  
        $scope.wfs.capabilitiesUrl = '';  
        $scope.wfs.featuresUrl = '';  
    }; 
    
    $scope.setData = function(wfs)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.wfs.id = wfs.id;
        $scope.wfs.name = wfs.name;
        $scope.wfs.description = wfs.description;
        $scope.wfs.capabilitiesUrl = wfs.capabilitiesUrl; 
        $scope.wfs.featuresUrl = wfs.featuresUrl; 

    };    

    $scope.getWfsList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/wfs')
            .success(function(data) 
            {     
                $scope.wfses = [];
                
                data.forEach(function(entry)
                {                
                    $scope.wfses.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    }; 
    
    $scope.saveWfs = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/wfs", $scope.wfs)
                .success(function() 
                {     
                    $scope.getWfsList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeWfs = function(id)
    {
        if (confirm("Are you sure?")) {
	    	$scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/wfs/'+id)
	            .success(function() 
	            {     
	                $scope.getWfsList(); 
	                $scope.alerts.push({type: 'success', msg: 'The record was deleted successfully!'});
	            })
	            .error(function(error)
	            {
	                $scope.alerts.push({type: 'danger', msg: error});
	            });     
        }
    };     
    
    $scope.closeAlert = function(index) 
    {
        $scope.alerts.splice(index, 1);
    };
    
    $scope.validate = function()
    {    
        if (!$scope.wfsForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.wfsForm.name.$invalid;            
            $scope.invalid.description = $scope.wfsForm.description.$invalid;
            $scope.invalid.capabilitiesUrl = $scope.wfsForm.capabilitiesUrl.$invalid;
            $scope.invalid.featuresUrl = $scope.wfsForm.featuresUrl.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.wfsForm.name.$valid;            
            $scope.valid.description = $scope.wfsForm.description.$valid;
            $scope.valid.capabilitiesUrl = $scope.wfsForm.capabilitiesUrl.$valid; 
            $scope.valid.featuresUrl = $scope.wfsForm.featuresUrl.$valid; 
            
            if ($scope.wfsForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.wfsForm.name.$error.required;
                $scope.help.name.minlength = $scope.wfsForm.name.$error.minlength;
            }  
            
            if ($scope.wfsForm.capabilitiesUrl.$invalid)
            {
                $scope.help.capabilitiesUrl = {};
                $scope.help.capabilitiesUrl.required = $scope.wfsForm.capabilitiesUrl.$error.required;
                $scope.help.capabilitiesUrl.minlength = $scope.wfsForm.capabilitiesUrl.$error.minlength;
            }   
            
            if ($scope.wfsForm.featuresUrl.$invalid)
            {
                $scope.help.featuresUrl = {};
                $scope.help.featuresUrl.required = $scope.wfsForm.featuresUrl.$error.required;
                $scope.help.featuresUrl.minlength = $scope.wfsForm.featuresUrl.$error.minlength;
            }             
        }  
        
        return $scope.wfsForm.$valid;
    };    
    
    $scope.getWfsList();     
    
}]);