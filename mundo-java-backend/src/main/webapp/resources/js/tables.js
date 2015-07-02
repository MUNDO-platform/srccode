var tablesApp = angular.module('TablesApp', ['ui.bootstrap']);

tablesApp.controller('TablesCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.tables = [];
    $scope.table = {};
    $scope.help = {};       
    $scope.alerts = []; 
    var dbName = $('#dbName').html();
    var dbId = $('#dbId').html();
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.table.id = '';
        $scope.table.dbId = dbId;
        $scope.table.name = '';
        $scope.table.cacheVariant = ''; 
        $scope.table.type = '';  
        $scope.table.params = '';  
    }; 
    
    $scope.setData = function(table)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.table.id = table.id;
        $scope.table.dbId = table.dbId;
        $scope.table.name = table.name;
        $scope.table.cacheVariant = table.cacheVariant;
        $scope.table.type = table.type;
        $scope.table.params = table.params; 
    };    

    $scope.getTableList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/db/'+dbName)
            .success(function(data) 
            {     
                $scope.tables = [];
                
                data.forEach(function(entry)
                {                
                    $scope.tables.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };
    
    $scope.saveTable = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/db/"+dbName, $scope.table)
                .success(function() 
                {     
                    $scope.getTableList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeTable = function(id)
    {
        if (confirm("Are you sure?")) {

	    	$scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/db/'+dbName+'/'+id)
	            .success(function() 
	            {     
	                $scope.getTableList(); 
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
        if (!$scope.tableForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.tableForm.name.$invalid;
            $scope.invalid.cacheVariant = $scope.tableForm.cacheVariant.$invalid;
            $scope.invalid.type = $scope.tableForm.type.$invalid;
            $scope.invalid.params = $scope.tableForm.params.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.tableForm.name.$valid;
            $scope.valid.cacheVariant = $scope.tableForm.cacheVariant.$valid;
            $scope.valid.type = $scope.tableForm.type.$valid;
            $scope.valid.params = $scope.tableForm.params.$valid; 
            
            if ($scope.tableForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.tableForm.name.$error.required;
                $scope.help.name.minlength = $scope.tableForm.name.$error.minlength;
            }                 
        }  
        
        return $scope.tableForm.$valid;
    };      
    
    $scope.getTableList();     
    
}]);