var queuesApp = angular.module('QueuesApp', ['ui.bootstrap']);

queuesApp.controller('QueuesCtrl',['$scope', '$http', function($scope, $http) 
{    
    $scope.queues = [];
    $scope.queue = {};
    $scope.help = {};    
    $scope.alerts = []; 
    
    $scope.clearData = function()
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};
        $scope.alerts = [];
        $scope.queue.id = '';
        $scope.queue.name = '';
        $scope.queue.description = '';  
        $scope.queue.url = '';  
    }; 
    
    $scope.setData = function(queue)
    {
        $scope.invalid = {};
        $scope.valid = {};
        $scope.help = {};        
        $scope.alerts = [];       
        $scope.queue.id = queue.id;
        $scope.queue.name = queue.name;
        $scope.queue.description = queue.description;
        $scope.queue.url = queue.url; 
    };    

    $scope.getQueueList = function()
    {
        $http.get('/cbr/mundo-java-backend/gui/user/queue')
            .success(function(data) 
            {     
                $scope.queues = [];
                
                data.forEach(function(entry)
                {                
                    $scope.queues.push(entry);
                });
            })
            .error(function(error)
            {
                $scope.alerts.push({type: 'danger', msg: error});
            });           
    }; 
    
    $scope.saveQueue = function()
    {
        if ($scope.validate())
        {
            $http.post("/cbr/mundo-java-backend/gui/admin/queue", $scope.queue)
                .success(function() 
                {     
                    $scope.getQueueList(); 
                    $scope.alerts.push({type: 'success', msg: 'The record was saved successfully!'});
                })
                .error(function(error)
                {
                    $scope.alerts.push({type: 'danger', msg: error});
                });

            $('#myModal').modal('hide');             
        }
    }; 
    
    $scope.removeQueue = function(id)
    {
        if (confirm("Are you sure?")) {

	        $scope.alerts = [];
	        
	        $http.delete('/cbr/mundo-java-backend/gui/admin/queue/'+id)
	            .success(function() 
	            {     
	                $scope.getQueueList(); 
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
        if (!$scope.queueForm.$valid)
        {
            $scope.help = {};
            $scope.invalid = {};
            $scope.invalid.name = $scope.queueForm.name.$invalid;            
            $scope.invalid.description = $scope.queueForm.description.$invalid;
            $scope.invalid.url = $scope.queueForm.url.$invalid;
            
            $scope.valid = {};
            $scope.valid.name = $scope.queueForm.name.$valid;            
            $scope.valid.description = $scope.queueForm.description.$valid;
            $scope.valid.url = $scope.queueForm.url.$valid; 
            
            if ($scope.queueForm.name.$invalid)
            {
                $scope.help.name = {};
                $scope.help.name.required = $scope.queueForm.name.$error.required;
                $scope.help.name.minlength = $scope.queueForm.name.$error.minlength;
            }  
            
            if ($scope.queueForm.url.$invalid)
            {
                $scope.help.url = {};
                $scope.help.url.required = $scope.queueForm.url.$error.required;
                $scope.help.url.minlength = $scope.queueForm.url.$error.minlength;
            }               
        }  
        
        return $scope.queueForm.$valid;
    };    
    
    $scope.getQueueList();     
    
}]);