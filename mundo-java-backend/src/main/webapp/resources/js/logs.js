var logsApp = angular.module('LogsApp', ['ui.bootstrap']);

logsApp.controller('LogsCtrl',['$scope', '$http', '$location', '$anchorScroll', function($scope, $http, $location, $anchorScroll) 
{    
    $scope.logs = [];  
    $scope.alerts = []; 
    $scope.page = 0;
    $scope.size = 100;     
 
    $scope.getLogList = function(page)
    {
        $http.get('/cbr/mundo-java-backend/gui/admin/log?page='+page+'&size='+$scope.size)
            .success(function(data) 
            {     
                $scope.logs = data;
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    };          
    
    $scope.closeAlert = function(index) 
    {
        $scope.alerts.splice(index, 1);
    }; 
    
    $scope.range = function(n) {
        return new Array(n);
    };     
    
    $scope.getLogList($scope.page);     
    
}]);

logsApp.filter('timestamp2date', function ($sce) {
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
