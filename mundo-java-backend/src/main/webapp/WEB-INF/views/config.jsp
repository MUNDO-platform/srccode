
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ page session="true"%>
<!DOCTYPE html>

<html ng-app="ConfigsApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Config</title>                              
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet"> 
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>        
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>        
        <script src="<c:url value="/resources/js/config.js" />"></script>  
    </head>
    
    <body ng-controller="ConfigsCtrl">      
        
        <jsp:include page="navbar.jsp" />                
        
        <div class="container-fluid">  
            
            <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')">
            <button ng-click="clearData()" class="btn btn-primary btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                <span class="glyphicon glyphicon-plus"></span> Add new
            </button> <br/><br/> 
            
            <alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</alert>            
            
            <div ng-click="" class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button ng-click="" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">Config data</h4>
                        </div>
                        <div class="modal-body">          
                            <form class="form-horizontal" name="configForm">                                    
                                <div class="form-group" ng-class="{ 'has-error': invalid.key, 'has-success': valid.key}">    
                                    <label for="key" class="col-sm-3 control-label">Key</label>
                                    <div class="col-sm-9">
                                        <input type="text" ng-model="config.key" name="key" class="form-control" ng-required="true" ng-minlength="2">
                                        <span id="helpBlock" class="help-block" ng-show="help.key.minlength">Key is too short.</span>                                               
                                        <span id="helpBlock" class="help-block" ng-show="help.key.required">Key is required.</span>                                                                                            
                                    </div>
                                </div>   
                                <div class="form-group" ng-class="{ 'has-error': invalid.value, 'has-success': valid.value}">    
                                    <label for="value" class="col-sm-3 control-label">Value</label>
                                    <div class="col-sm-9">
                                        <input type="text" ng-model="config.value" name="value" class="form-control" ng-required="true" ng-minlength="2">
                                        <span id="helpBlock" class="help-block" ng-show="help.value.minlength">Value is too short.</span>                                               
                                        <span id="helpBlock" class="help-block" ng-show="help.value.required">Value is required.</span>                                                                                            
                                    </div>
                                </div>                                 
                                <div class="form-group" ng-class="{ 'has-error': invalid.description, 'has-success': valid.description}">    
                                    <label for="description" class="col-sm-3 control-label">Description</label>
                                    <div class="col-sm-9">
                                        <input type="text" ng-model="config.description" name="description" class="form-control">                                                                                        
                                    </div>
                                </div>                                                                       
                                <div class="modal-footer">
                                    <div class="form-group">                        
                                        <button type="submit" ng-click="saveConfig()" class="btn btn-success"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Save</button>                                                    
                                        <button ng-click="" type="button" class="btn btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Close</button>
                                    </div>
                                </div>
                            </form>    
                        </div>
                    </div>
                </div>                                        
            </div>
            </security:authorize> 
            
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Key</th>
                        <th>Value</th> 
                        <th>Description</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="config in configs">
                        <td>{{config.id}}</td>
                        <td>{{config.key}}</td>
                        <td>{{config.value}}</td>
                        <td>{{config.description}}</a></td>
                        <td>
                            <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')">
                                <button ng-click="setData(config)" class="btn btn-default btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                                    <span class="glyphicon glyphicon-pencil"></span> Update
                                </button>  
                                <button type="button" ng-click="removeConfig(config.id)" class="btn btn-default btn-sm">
                                    <span class="glyphicon glyphicon-trash"></span> Remove
                                </button>                                                            
                            </security:authorize>                               
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>          
    </body>
</html>
