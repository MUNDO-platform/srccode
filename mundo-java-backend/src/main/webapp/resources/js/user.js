var userApp = angular.module('UserApp', ['ui.bootstrap']);

userApp.controller('UserCtrl',['$scope', '$http', '$location', '$anchorScroll', function($scope, $http, $location, $anchorScroll) 
{    
    var username = $('#username').html();
    $scope.user = {};
    $scope.alerts = []; 
    $scope.help = {};

    $scope.notthesame = false;
    
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

//    $scope.getMsisdn = function()
//    {          
//    };
  
    $scope.getAccountDetails = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/accountdetails/'+username)
            .success(function(data) 
            {     
                $scope.user = data;
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };    

    $scope.saveAccount = function()
    {
        if ($scope.validate())
        {
        	$scope.notthesame = false;
            $http.put("/cbr/mundo-java-backend/gui/user/saveaccount", $scope.user)
                .success(function() 
                {   
                    $scope.alerts.push({type: 'success', msg: 'The record was updated successfully!'});
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
            $scope.invalid.password = $scope.userForm.password.$invalid;
            
            $scope.valid = {};
            $scope.valid.password = $scope.userForm.password.$valid;
            
            if ($scope.userForm.password.$invalid)
            {
                $scope.help.password = {};
                $scope.help.password.required = $scope.userForm.password.$error.required;
                $scope.help.password.minlength = $scope.userForm.password.$error.minlength;                
            }
        }  

        if (($scope.user.repassword != $scope.user.password)) {
        	$scope.notthesame = true;
        	return false;
        }
        
        return $scope.userForm.$valid;
    };

    
//    $scope.getMsisdn();
    
}]);

userApp.filter('timestamp2date', function ($sce) {
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
