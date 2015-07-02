var dbsApp = angular.module('DbsApp', ['ui.bootstrap']);

dbsApp.controller('DbsCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.dbs = [];
    $scope.db = {};
    $scope.help = {};       
    $scope.alerts = []; 
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.db.id = '';
        $scope.db.name = '';
        $scope.db.description = '';  
        $scope.db.jndiName = '';  
    }; 
    
    $scope.setData = function(db)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.db.id = db.id;
        $scope.db.name = db.name;
        $scope.db.description = db.description;
        $scope.db.jndiName = db.jndiName; 
    };    

    $scope.getDbList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/db')
            .success(function(data) 
            {     
                $scope.dbs = [];
                
                data.forEach(function(entry)
                {                
                    $scope.dbs.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };
    
    $scope.saveDb = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/db", $scope.db)
                .success(function() 
                {     
                    $scope.getDbList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeDb = function(id)
    {
        if (confirm("Are you sure?")) {

	        $scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/db/'+id)
	            .success(function() 
	            {     
	                $scope.getDbList(); 
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
        if (!$scope.dbForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.dbForm.name.$invalid;            
            $scope.invalid.description = $scope.dbForm.description.$invalid;
            $scope.invalid.jndiName = $scope.dbForm.jndiName.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.dbForm.name.$valid;            
            $scope.valid.description = $scope.dbForm.description.$valid;
            $scope.valid.jndiName = $scope.dbForm.jndiName.$valid; 
            
            if ($scope.dbForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.dbForm.name.$error.required;
                $scope.help.name.minlength = $scope.dbForm.name.$error.minlength;
            }  
            
            if ($scope.dbForm.jndiName.$invalid)
            {
                $scope.help.jndiName = {};
                $scope.help.jndiName.required = $scope.dbForm.jndiName.$error.required;
                $scope.help.jndiName.minlength = $scope.dbForm.jndiName.$error.minlength;
            }               
        }  
        
        return $scope.dbForm.$valid;
    };      
    
    $scope.getDbList();     
    
}]);