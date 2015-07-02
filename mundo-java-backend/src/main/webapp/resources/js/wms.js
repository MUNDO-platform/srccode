var wmsApp = angular.module('WmsApp', ['ui.bootstrap']);

wmsApp.controller('WmsCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.wmses = [];
    $scope.wms = {};
    $scope.help = {};    
    $scope.alerts = []; 
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.wms.id = '';
        $scope.wms.name = '';
        $scope.wms.description = '';  
        $scope.wms.url = '';
        $scope.wms.srs = 'EPSG:2178';
        $scope.wms.latitude = '52.240616';
        $scope.wms.longitude = '20.998012';
    }; 
    
    $scope.setData = function(wms)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.wms.id = wms.id;
        $scope.wms.name = wms.name;
        $scope.wms.description = wms.description;
        $scope.wms.url = wms.url;
        $scope.wms.srs = wms.srs;
        $scope.wms.latitude = wms.latitude;
        $scope.wms.longitude = wms.longitude;

    };    

    $scope.getWmsList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/wms')
            .success(function(data) 
            {     
                $scope.wmses = [];
                
                data.forEach(function(entry)
                {                
                    $scope.wmses.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    }; 
    
    $scope.saveWms = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/wms", $scope.wms)
                .success(function() 
                {     
                    $scope.getWmsList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeWms = function(id)
    {
        if (confirm("Are you sure?")) {

	    	$scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/wms/'+id)
	            .success(function() 
	            {     
	                $scope.getWmsList(); 
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
        if (!$scope.wmsForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.wmsForm.name.$invalid;            
            $scope.invalid.description = $scope.wmsForm.description.$invalid;
            $scope.invalid.url = $scope.wmsForm.url.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.wmsForm.name.$valid;            
            $scope.valid.description = $scope.wmsForm.description.$valid;
            $scope.valid.url = $scope.wmsForm.url.$valid; 
            
            if ($scope.wmsForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.wmsForm.name.$error.required;
                $scope.help.name.minlength = $scope.wmsForm.name.$error.minlength;
            }  
            
            if ($scope.wmsForm.url.$invalid)
            {
                $scope.help.url = {};
                $scope.help.url.required = $scope.wmsForm.url.$error.required;
                $scope.help.url.minlength = $scope.wmsForm.url.$error.minlength;
            }               
        }  
        
        return $scope.wmsForm.$valid;
    };    
    
    $scope.getWmsList();     
    
}]);