    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    <nav class="navbar navbar-default" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>        
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li ng-class="checkTabClass('map')"><a href="#" ng-click="setTab('map')"><span class="glyphicon glyphicon-globe"></span> Map</a></li>
                    <li ng-class="checkTabClass('grid')"><a href="#" ng-click="setTab('grid')"><span class="glyphicon glyphicon-calendar"></span> Grid</a></li>                    
                </ul>                  
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="#" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-filter"></span> Filter</a></li>                        
                </ul>               
            </div>
        </div>
    </nav>  
                
    <!-- Modal Filter -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">                    
                    Filter settings
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4"><label>latitude</label></div>    
                        <div class="col-md-4"><input type="text" class="form-control" ng-model="latitude" placeholder="{{latitude}}"></div>
                    </div>
                    <div class="row">
                        <div class="col-md-4"><label>longitude</label></div>    
                        <div class="col-md-4"><input type="text" class="form-control" ng-model="longitude" placeholder="{{longitude}}"></div>
                    </div>                    
                    <div class="row">                     
                        <div class="col-md-4"><label>maxFeatures</label></div>
                        <div class="col-md-4"><input type="text" class="form-control" ng-model="maxFeatures" placeholder="{{maxFeatures}}"></div>
                    </div>
                    <div class="row">                     
                        <div class="col-md-4"><label>radius [m]</label></div>
                        <div class="col-md-4"><input type="text" class="form-control" ng-model="radius" placeholder="{{radius}}"></div>
                    </div>   
                    <br/>
                    <button ng-click="getData()" class="btn btn-default" data-dismiss="modal">Submit</button>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>                