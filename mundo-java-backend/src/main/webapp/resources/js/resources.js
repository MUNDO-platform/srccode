var resourcesApp = angular.module('ResourcesApp', ['ui.bootstrap']);

resourcesApp.controller('ResourcesCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.resources = [];
    $scope.resource = {};
    $scope.help = {};    
    $scope.alerts = []; 
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.resource.id = '';
        $scope.resource.name = '';
        $scope.resource.description = '';  
        $scope.resource.url = '';  
    }; 
    
    $scope.setData = function(resource)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.resource.id = resource.id;
        $scope.resource.name = resource.name;
        $scope.resource.description = resource.description;
        $scope.resource.url = resource.url; 

    };    

    $scope.getResourceList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/resource')
            .success(function(data) 
            {     
                $scope.resources = [];
                
                data.forEach(function(entry)
                {                
                    $scope.resources.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    }; 
    
    $scope.saveResource = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/resource", $scope.resource)
                .success(function() 
                {     
                    $scope.getResourceList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeResource = function(id)
    {
        if (confirm("Are you sure?")) {

	        $scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/resource/'+id)
	            .success(function() 
	            {     
	                $scope.getResourceList(); 
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
        if (!$scope.resourceForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.resourceForm.name.$invalid;            
            $scope.invalid.description = $scope.resourceForm.description.$invalid;
            $scope.invalid.url = $scope.resourceForm.url.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.resourceForm.name.$valid;            
            $scope.valid.description = $scope.resourceForm.description.$valid;
            $scope.valid.url = $scope.resourceForm.url.$valid; 
            
            if ($scope.resourceForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.resourceForm.name.$error.required;
                $scope.help.name.minlength = $scope.resourceForm.name.$error.minlength;
            }  
            
            if ($scope.resourceForm.url.$invalid)
            {
                $scope.help.url = {};
                $scope.help.url.required = $scope.resourceForm.url.$error.required;
                $scope.help.url.minlength = $scope.resourceForm.url.$error.minlength;
            }             
        }  
        
        return $scope.resourceForm.$valid;
    };    
    
    $scope.getResourceList();     
    
}]);