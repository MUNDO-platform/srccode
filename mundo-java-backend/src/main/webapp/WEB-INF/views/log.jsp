<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@page session="true"%>
<!DOCTYPE html>

<html ng-app="LogsApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Logs</title>       
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet"> 
        <!-- Placed at the end of the document so the .js"pages load faster -->
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>        
        <script src="<c:url value="/resources/js/logs.js" />"></script>        
    </head>
    
    <body ng-controller="LogsCtrl">  
        
        <jsp:include page="navbar.jsp" />    
        
        <div class="container">
            <div class="container-fluid">

                <alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</alert>              
                
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Method</th>
                                <th>Account</th>
                                <th>Stamp</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="log in logs.content">
                                <td>{{log.id}}</td>
                                <td>{{log.method}}</td>
                                <td>{{log.account}}</td>
                                <td>{{log.stamp | timestamp2date}}</td>                                
                            </tr>
                        </tbody>
                    </table>  
                    <nav>
                      <ul class="pagination">
                        <li ng:class="{true:'active', false:''}[$index==logs.number]" ng-repeat="a in range(logs.totalPages) track by $index"><a ng-click="getLogList($index)" href="#">{{$index + 1}}</a></li>
                      </ul>
                    </nav>                      
                </div>    
            </div>    
        </div>        
    </body>
</html>
