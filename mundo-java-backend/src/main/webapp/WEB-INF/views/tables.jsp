
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@page session="true"%>
<!DOCTYPE html>

<html ng-app="TablesApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Tables</title>                              
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet"> 
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>        
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>        
        <script src="<c:url value="/resources/js/tables.js" />"></script>         
    </head>
    
    <body ng-controller="TablesCtrl">
        
        <span id="dbName" ng-show="dbName">${dbEntity.name}</span>
        <span id="dbId" ng-show="dbId">${dbEntity.id}</span>
        
        <jsp:include page="navbar.jsp" /> 
          
        <div class="container-fluid">
            
            <ol class="breadcrumb">
              <li><a href="${pageContext.request.contextPath}/gui/user/home">Home</a></li>
              <li><a href="${pageContext.request.contextPath}/gui/user/db">DB</a></li>
              <li class="active">${dbEntity.name}</li>
            </ol> 
            
            <security:authorize access="hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')">
            <button ng-click="clearData()" class="btn btn-primary btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                <span class="glyphicon glyphicon-plus"></span> Add new
            </button> <br/><br/> 
            
            <alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</alert>
                             
            <div ng-click="" class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button ng-click="" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">Table data</h4>
                        </div>
                        <div class="modal-body">          
                            <form class="form-horizontal" name="tableForm">                                    
                                <div class="form-group" ng-class="{ 'has-error': invalid.name, 'has-success': valid.name}">    
                                    <label for="name" class="col-sm-3 control-label">Name</label>
                                    <div class="col-sm-9">
                                        <input type="text" ng-model="table.name" name="name" class="form-control" ng-required="true" ng-minlength="2">
                                        <span id="helpBlock" class="help-block" ng-show="help.name.minlength">Name is too short.</span>                                               
                                        <span id="helpBlock" class="help-block" ng-show="help.name.required">Name is required.</span>                                                                                            
                                    </div>
                                </div>   
                                <div class="form-group" ng-class="{ 'has-error': invalid.type, 'has-success': valid.type}">    
                                    <label for="type" class="col-sm-3 control-label">Type</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" ng-model="table.type" name="type" required>
                                            <option value="table">Table</option>
                                            <option value="procedure">Procedure</option>
                                            <option value="view">View</option>
                                        </select> 
                                        <span id="helpBlock" class="help-block" ng-show="help.type.required">Type is required.</span> 
                                    </div>
                                </div> 
                                <div class="form-group" ng-class="{ 'has-error': invalid.cacheVariant, 'has-success': valid.cacheVariant}">    
                                    <label for="cacheVariant" class="col-sm-3 control-label">CacheVariant</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" ng-model="table.cacheVariant" name="cacheVariant" required>
                                            <option value="0">0-OFF</option>
                                            <option value="60">60-1min</option>
                                            <option value="3600">3600-1hour</option>
                                            <option value="43200">43200-12hours</option>
                                            <option value="86400">86400-24hours</option>
                                        </select> 
                                        <span id="helpBlock" class="help-block" ng-show="help.type.required">CaheVariant is required.</span> 
                                    </div>
                                </div> 
                                <div class="form-group" ng-class="{ 'has-error': invalid.params, 'has-success': valid.params}">    
                                    <label for="params" class="col-sm-3 control-label">Params</label>
                                    <div class="col-sm-9">
                                        <input type="text" ng-model="table.params" name="params" class="form-control" ng-required="true" ng-minlength="3">                                                                                        
                                        <span id="helpBlock" class="help-block" ng-show="help.params.minlength">Params is too short.</span>                                               
                                        <span id="helpBlock" class="help-block" ng-show="help.params.required">Params is required.</span>  
                                    </div>
                                </div>                                  
                                <div class="modal-footer">
                                    <div class="form-group">                        
                                        <button type="submit" ng-click="saveTable()" class="btn btn-success"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Save</button>                                                    
                                        <button ng-click="" type="button" class="btn btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Close</button>
                                    </div>
                                </div>
                            </form>    
                        </div>
                    </div>
                </div>                                        
            </div>  
            </security:authorize>              
            
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Type</th>
                        <th>CacheVariant</th>
                        <th>Params</th>
                        <th>REST URL</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="table in tables">
                        <td>{{table.id}}</td>
                        <td><a href="${pageContext.request.contextPath}/gui/user/db/${dbEntity.name}/{{table.name}}">{{table.name}}</a></td>
                        <td>{{table.type}}</td>
                        <td>{{table.cacheVariant}}</td> 
                        <td>{{table.params}}</td> 
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/db/${dbEntity.name}/{{table.name}}">JSON</a></td>
                        <td>
                            <security:authorize access="hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')">
                                <button ng-click="setData(table)" class="btn btn-default btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                                    <span class="glyphicon glyphicon-pencil"></span> Update
                                </button>  
                                <button type="button" ng-click="removeTable(table.id)" class="btn btn-default btn-sm">
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