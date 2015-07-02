var mundoApp = angular.module('MundoApp', ['ui.bootstrap', 'ui.grid', 'ui.grid.resizeColumns']);

mundoApp.controller('MundoCtrl',['$scope', '$http', '$location', '$anchorScroll', function($scope, $http, $location, $anchorScroll) 
{    
    $scope.wfsObj = [];
    $scope.coordinates = [];
    
    var data = [];
    var namespace = $('#namespace').html();
    var name = $('#name').html();
    var wfsId = $('#wfsId').html(); 
    
    $scope.checkTab = 'map'; 
    $scope.maxFeatures = 100;
    $scope.latitude = 52.240616;
    $scope.longitude = 20.998012;
    $scope.radius = 10000;
    
    $scope.gridOptions = {
        showFooter: true,
        enableFiltering: true,
        enableColumnResizing: true,
        data: data
    };
    
    $scope.setTab = function(tab) 
    {
        $scope.checkTab = tab; 
    }; 
    
    $scope.checkTabClass = function(tab) 
    {
        if ($scope.checkTab == tab)
        {
            return 'active';
        }
    };
    
    $scope.getFilterString = function(key,value) 
    {
        return '';
    };     
    
    $scope.getData = function() 
    {
        //var map = document.querySelector('google-map');
        //map.clear();
        //
        //$scope.latitude = map.latitude;
        //$scope.longitude = map.longitude;
        //$scope.zoom = map.zoom;
        var mapcenter = $scope.map.getCenter();
        $scope.latitude = mapcenter.getLatitude();
        $scope.longitude = mapcenter.getLongitude();
        $scope.center =new MQA.Poi({lat:$scope.latitude, lng:$scope.longitude});
        $scope.center.setRolloverContent('latitude:'+$scope.latitude+', longitude:'+$scope.longitude);
        $scope.center.setIcon($scope.icon);
        $scope.map.removeAllShapes();
        
        $http.get('/cbr/mundo-java-backend/wfs/'+wfsId+'/rest/capabilities/'+namespace+'/'+name+'.props?maxFeatures='+$scope.maxFeatures+'&circle='+$scope.longitude+','+$scope.latitude+','+$scope.radius)
            .success(function(data) 
            {         
                $scope.gridOptions.data = data;           
            })
            .error(function(error)
            {
                alert(error);
            });
            
        $http.get('/cbr/mundo-java-backend/wfs/'+wfsId+'/rest/capabilities/'+namespace+'/'+name+'.coordinates?maxFeatures='+$scope.maxFeatures+'&circle='+$scope.longitude+','+$scope.latitude+','+$scope.radius)
            .success(function(data) 
            {         
                $scope.coordinates = data;  
                
                $scope.collection.removeAll();
                $scope.coordinates.forEach(function(entry)
                {                              
                    $scope.collection.add(new MQA.Poi({lat:entry.latitude, lng:entry.longitude}));
                });
                $scope.map.addShapeCollection($scope.collection); 
                $scope.map.addShape($scope.center);
            })
            .error(function(error)
            {
                alert(error);
            });            
    };     
    
    $http.get('/cbr/mundo-java-backend/wfs/'+wfsId+'/rest/capabilities/'+namespace+'/'+name+'.props?maxFeatures='+$scope.maxFeatures+'&circle='+$scope.longitude+','+$scope.latitude+','+$scope.radius)
        .success(function(data) 
        {         
            $scope.gridOptions.data = data;           
        })
        .error(function(error)
        {
            alert(error);
        }); 
        
    $http.get('/cbr/mundo-java-backend/wfs/'+wfsId+'/rest/capabilities/'+namespace+'/'+name+'.coordinates?maxFeatures='+$scope.maxFeatures+'&circle='+$scope.longitude+','+$scope.latitude+','+$scope.radius)
        .success(function(data) 
        {         
            $scope.coordinates = data; 
            
            // create an object for options
            $scope.options = {
                elt: document.getElementById('map'),             // ID of map element on page
                zoom: 12,                                        // initial zoom level of the map
                latLng: { lat: 52.240616, lng: 20.998012 }     // center of map in latitude/longitude
            };  
                
            $scope.map = new MQA.TileMap($scope.options);
            $scope.collection = new MQA.ShapeCollection();
                
            $scope.coordinates.forEach(function(entry)
            {                              
                $scope.collection.add(new MQA.Poi({lat:entry.latitude, lng:entry.longitude}));
            });
            $scope.map.addShapeCollection($scope.collection);
            
            $scope.center =new MQA.Poi({lat:52.240616, lng:20.998012});
            $scope.center.setRolloverContent('latitude:'+52.240616+', longitude:'+20.998012);
            $scope.icon=new MQA.Icon("http://open.mapquestapi.com/staticmap/geticon?uri=poi-red_1.png",20,29);
            $scope.center.setIcon($scope.icon);
            $scope.map.addShape($scope.center);
        })
        .error(function(error)
        {
            alert(error);
        }); 
}]);

