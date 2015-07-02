<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@page session="true"%>
<!DOCTYPE html>

<html ng-app="AccountsApp">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Accounts</title>       
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet"> 
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>        
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>        
        <script src="<c:url value="/resources/js/accounts.js" />"></script>        
    </head>
    
    <body ng-controller="AccountsCtrl">  
        
        <jsp:include page="navbar.jsp" />    
        
        <div class="container">
            <div class="container-fluid">

                <alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert($index)">{{alert.msg}}</alert>
                
                <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')">
                <button ng-click="clearData()" class="btn btn-default btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                    <span class="glyphicon glyphicon-cog"></span> New Account
                </button> <br/><br/>
                </security:authorize>                
                
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Username</th>
                                <th>First name</th>
                                <th>Last name</th>
                                <th>Email</th>
                                <th>Type</th>
                                <th>Action</th>                               
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="account in accounts">
                                <td>{{account.id}}</td>
                                <td>{{account.username}}</td>
                                <td>{{account.firstName}}</td>
                                <td>{{account.lastName}}</td>
                                <td>{{account.email}}</td>
                                <td>{{account.type}}</td>
                                <td>
                                    <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')">
                                    <button type="button" ng-click="removeAccount(account.id)" class="btn btn-default btn-sm">
                                        <span class="glyphicon glyphicon-trash"></span> Remove
                                    </button>
                                    </security:authorize>  
                                    <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')">
                                    <button ng-click="setData(account)" class="btn btn-default btn-sm" type="button" data-toggle="modal" data-target="#myModal">
                                        <span class="glyphicon glyphicon-pencil"></span> Update
                                    </button>  
                                    </security:authorize>                                    
                                </td>                                
                            </tr>
                        </tbody>
                    </table> 
                    
                    <div ng-click="" class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button ng-click="" type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                    <h4 class="modal-title" id="myModalLabel">Account data</h4>
                                </div>
                                <div class="modal-body">          
                                    <form class="form-horizontal" name="userForm">                                    
                                        <div class="form-group" ng-class="{ 'has-error': invalid.username, 'has-success': valid.username}">    
                                            <label for="username" class="col-sm-3 control-label">Username</label>
                                            <div class="col-sm-9">
                                                <input type="text" ng-model="user.username" name="username" class="form-control" ng-required="true" ng-minlength="3">
                                                <span id="helpBlock" class="help-block" ng-show="help.username.minlength">Username is too short.</span>                                               
                                                <span id="helpBlock" class="help-block" ng-show="help.username.required">Username is required.</span>                                                                                            
                                            </div>
                                        </div>   
                                        <div class="form-group">    
                                            <label for="firstName" class="col-sm-3 control-label">First name</label>
                                            <div class="col-sm-9">
                                                <input type="text" ng-model="user.firstName" name="firstName" class="form-control">                                                                                        
                                            </div>
                                        </div> 
                                        <div class="form-group">    
                                            <label for="lastName" class="col-sm-3 control-label">Last name</label>
                                            <div class="col-sm-9">
                                                <input type="text" ng-model="user.lastName" name="lastName" class="form-control">                                                                                        
                                            </div>
                                        </div> 
                                        <div class="form-group">    
                                            <label for="email" class="col-sm-3 control-label">Email</label>
                                            <div class="col-sm-9">
                                                <input type="text" ng-model="user.email" name="email" class="form-control">                                                                                        
                                            </div>
                                        </div> 
                                        <div class="form-group"  ng-class="{ 'has-error': invalid.password, 'has-success': valid.password}">    
                                            <label for="password" class="col-sm-3 control-label">Password</label>
                                            <div class="col-sm-9">
                                                <input type="password" ng-model="user.password" name="password" class="form-control" required ng-minlength="3">
                                                <span id="helpBlock" class="help-block" ng-show="help.password.minlength">Password is too short.</span>                                               
                                                <span id="helpBlock" class="help-block" ng-show="help.password.required">Password is required.</span>                                                                                            
                                            </div>
                                        </div>   
                                        <div class="form-group"  ng-class="{ 'has-error': invalid.type, 'has-success': valid.type}">    
                                            <label for="username" class="col-sm-3 control-label">Type</label>
                                            <div class="col-sm-9">
                                                <select class="form-control" ng-model="user.type" name="type" required>
                                                    <option value="ROLE_USER">User</option>
                                                    <option value="ROLE_ADMIN">Admin</option>
                                                    <option value="ROLE_SUPERADMIN">Superadmin</option>
                                                </select>
                                                <span id="helpBlock" class="help-block" ng-show="help.type.required">Type is required.</span>                                                                                            
                                            </div>
                                        </div>                                                                         
                                        <div class="modal-footer">
                                            <div class="form-group">                        
                                                <button type="submit" ng-click="saveAccount()" class="btn btn-success"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Save</button>                                                    
                                                <button ng-click="" type="button" class="btn btn-danger" data-dismiss="modal"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Close</button>
                                            </div>
                                        </div>
                                    </form>    
                                </div>
                            </div>
                        </div>                                        
                    </div>                     
                </div>    
            </div> 
        </div>   
    </body>
</html>
