var statisticsApp = angular.module('StatisticsApp', ['ui.bootstrap']);

statisticsApp.controller('StatisticsCtrl',['$scope', '$http', '$location', '$anchorScroll', function($scope, $http, $location, $anchorScroll) 
{    
    $scope.statistics = [];  
    $scope.alerts = [];    
 
    $scope.getStatList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/admin/statistics')
            .success(function(data) 
            {     
                $scope.statistics = data;
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
    
    $scope.getStatList();     
    
}]);

statisticsApp.filter('timestamp2date', function ($sce) {
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
