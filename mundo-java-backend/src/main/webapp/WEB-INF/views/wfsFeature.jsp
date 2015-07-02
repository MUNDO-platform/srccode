<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page session="true"%>
<!DOCTYPE html>

<html ng-app="MundoApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Home Page</title>   
        <%--
        <script src="<c:url value="/resources/webcomponents/platform/platform.js" />"></script>
        --%>
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>
        <script src="<c:url value="/resources/js/ui-grid-stable.min.js" />"></script>        
        <script src="<c:url value="/resources/js/mundo.js" />"></script>
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/ui-grid-stable.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/mundo.css" />" rel="stylesheet">
        <%--
        <link rel="import" href="<c:url value="/resources/webcomponents/google-map/google-map.html" />">     
        --%>
        <script src="http://open.mapquestapi.com/sdk/js/v7.2.s/mqa.toolkit.js?key=Fmjtd%7Cluurn96b2l%2C7w%3Do5-9w8s90"></script>
    </head>
    
    <body ng-controller="MundoCtrl">     
        <span id="namespace" ng-show="namespace">${namespace}</span>
        <span id="name" ng-show="name">${name}</span>
        <span id="wfsId" ng-show="wfsId">${wfsId}</span>
        
        <jsp:include page="wfsFeatureNavbar.jsp" />  
        
        
        
            <div class="container-fluid">    
                <div id="map" style="width:850px; height:500px;" ng-show="checkTab=='map'"></div>
                <%--
                <google-map  longitude="20.998012" latitude="52.240616" ng-show="checkTab=='map'">
                    <google-map-marker ng-repeat="point in coordinates" latitude="{{point.latitude}}" longitude="{{point.longitude}}"></google-map-marker>
                </google-map>
                --%>
                <div ui-grid="gridOptions" class="grid" ui-grid-resize-columns ng-show="checkTab=='grid'"></div>
            </div>          
    </body>
</html>