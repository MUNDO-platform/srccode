var columnsApp = angular.module('ColumnsApp', ['ui.bootstrap']);

columnsApp.controller('ColumnsCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.columns = [];
    $scope.column = {};
    $scope.help = {};       
    $scope.alerts = []; 
    var dbName = $('#dbName').html();
    var dbId = $('#dbId').html();
    var tabName = $('#tableName').html();
    var tabId = $('#tableId').html();    
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.column.id = '';
        $scope.column.tabId = tabId;
        $scope.column.name = '';
    }; 
    
    $scope.setData = function(column)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.column.id = column.id;
        $scope.column.tabId = column.tabId;
        $scope.column.name = column.name;
    };    

    $scope.getColumnList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/db/'+dbName+'/'+tabName)
            .success(function(data) 
            {     
                $scope.columns = [];
                
                data.forEach(function(entry)
                {                
                    $scope.columns.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };
    
    $scope.saveColumn = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/db/"+dbName+"/"+tabName, $scope.column)
                .success(function() 
                {     
                    $scope.getColumnList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeColumn = function(id)
    {
        if (confirm("Are you sure?")) {

	    	$scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/db/'+dbName+'/'+tabName+'/'+id)
	            .success(function() 
	            {     
	                $scope.getColumnList(); 
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
        if (!$scope.columnForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.columnForm.name.$invalid;            
            
            $scope.valid = {};
            $scope.valid.name = $scope.columnForm.name.$valid;            
            
            if ($scope.columnForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.columnForm.name.$error.required;
                $scope.help.name.minlength = $scope.columnForm.name.$error.minlength;
            }                 
        }  
        
        return $scope.columnForm.$valid;
    };      
    
    $scope.getColumnList();     
    
}]);