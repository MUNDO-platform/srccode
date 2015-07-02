var accountsApp = angular.module('AccountsApp', ['ui.bootstrap']);

accountsApp.controller('AccountsCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.accounts = [];
    $scope.user = {};
    $scope.help = {};
    $scope.alerts = []; 
       
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.user.id = '';
        $scope.user.username = '';
        $scope.user.password = '';  
        $scope.user.firstName = '';  
        $scope.user.lastName = '';  
        $scope.user.email = '';  
        $scope.user.type = '';
    }; 
    
    $scope.setData = function(account)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.user.id = account.id;
        $scope.user.username = account.username;
        $scope.user.password = account.password;
        $scope.user.firstName = account.firstName; 
        $scope.user.lastName = account.lastName; 
        $scope.user.email = account.email; 
        $scope.user.type = account.type;
    };    

    $scope.getAccountList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/superadmin/account')
            .success(function(data) 
            {     
                $scope.accounts = [];
                
                data.forEach(function(entry)
                {                
                    $scope.accounts.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };    
    
    $scope.removeAccount = function(id)
    {
    	if (confirm("Do you really want to remove account?")) {
    		
        $scope.alerts = [];
        
        $http.delete('/cbr/mundo-java-backend/gui/superadmin/account/'+id)
            .success(function() 
            {     
                $scope.getAccountList(); 
                $scope.alerts.push({type: 'success', msg: 'The record was deleted successfully!'});
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });     
       	}
    }; 
    
    $scope.saveAccount = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/superadmin/account", $scope.user)
                .success(function() 
                {     
                    $scope.getAccountList(); 
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
        if (!$scope.userForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.username = $scope.userForm.username.$invalid;            
            $scope.invalid.password = $scope.userForm.password.$invalid;
            $scope.invalid.type = $scope.userForm.type.$invalid;
            
            $scope.valid = {};
            $scope.valid.username = $scope.userForm.username.$valid;            
            $scope.valid.password = $scope.userForm.password.$valid;
            $scope.valid.type = $scope.userForm.type.$valid; 
            
            if ($scope.userForm.username.$invalid)
            {
                $scope.help.username = {};
                $scope.help.username.required = $scope.userForm.username.$error.required;
                $scope.help.username.minlength = $scope.userForm.username.$error.minlength;
            }
            
            if ($scope.userForm.password.$invalid)
            {
                $scope.help.password = {};
                $scope.help.password.required = $scope.userForm.password.$error.required;
                $scope.help.password.minlength = $scope.userForm.password.$error.minlength;                
            }
            
            if ($scope.userForm.type.$invalid)
            {
                $scope.help.type = {};
                $scope.help.type.required = $scope.userForm.type.$error.required;                
            }            
        }  
        
        return $scope.userForm.$valid;
    };
    
    $scope.getAccountList();     
    
}]);
