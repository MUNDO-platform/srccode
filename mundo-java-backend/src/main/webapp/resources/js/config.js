var configsApp = angular.module('ConfigsApp', ['ui.bootstrap']);

configsApp.controller('ConfigsCtrl',['$scope', '$http', '$location', '$anchorScroll', function($scope, $http, $location, $anchorScroll) 
{    
    $scope.configs = [];  
    $scope.config = {};
    $scope.help = {};
    $scope.alerts = [];  
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];
        $scope.config.id = '';
        $scope.config.key = '';
        $scope.config.value = '';       
        $scope.config.description = '';
    }; 
    
    $scope.setData = function(config)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];
        $scope.config.id = config.id;
        $scope.config.key = config.key;
        $scope.config.value = config.value;       
        $scope.config.description = config.description;
    };    

    $scope.getConfigList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/superadmin/config')
            .success(function(data) 
            {     
                $scope.configs = [];
                
                data.forEach(function(entry)
                {                
                    $scope.configs.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };    
    
    $scope.removeConfig = function(id)
    {
        if (confirm("Are you sure?")) {

	        $http.delete('/cbr/mundo-java-backend/gui/superadmin/config/'+id)
	            .success(function(data) 
	            {     
	                $scope.getConfigList(); 
	                $scope.alerts.push({type: 'success', msg: 'The record was deleted successfully!'});
	            })
	            .error(function(error)
	            {
	                $scope.alerts.push({type: 'danger', msg: error});
	            });     
        }
    };      
    
    $scope.saveConfig = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/superadmin/config", $scope.config)
                .success(function() 
                {     
                    $scope.getConfigList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });             
                
            $('#myModal').modal('hide');                 
        }
    };        
    
    $scope.closeAlert = function(index) 
    {
        $scope.alerts.splice(index, 1);
    };
    
    $scope.validate = function()
    {    
        if (!$scope.configForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.key = $scope.configForm.key.$invalid;            
            $scope.invalid.value = $scope.configForm.value.$invalid;
            $scope.invalid.description = $scope.configForm.description.$invalid;
            
            $scope.valid = {};
            $scope.valid.key = $scope.configForm.key.$valid;            
            $scope.valid.value = $scope.configForm.value.$valid;
            $scope.valid.description = $scope.configForm.description.$valid; 
            
            if ($scope.configForm.key.$invalid)
            {
                $scope.help.key = {};
                $scope.help.key.required = $scope.configForm.key.$error.required;
            }
            
            if ($scope.configForm.value.$invalid)
            {
                $scope.help.value = {};
                $scope.help.value.required = $scope.configForm.value.$error.required;
            }           
        }  
        
        return $scope.configForm.$valid;
    };    
    
    $scope.getConfigList();     
    
}]);

configsApp.filter('timestamp2date', function ($sce) {
    return function (text) 
    {
        var a = new Date(text);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        var sec = a.getSeconds();
        var time = date + ',' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec ;
        return text ? $sce.trustAsHtml(time) : '';
    };
});
