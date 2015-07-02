<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Home Page</title>                         
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script> 
        <script src="<c:url value="/resources/js/angular.min.js" />"></script> 
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/mundo.css" />" rel="stylesheet">
    </head>
    <body>
        
        <jsp:include page="navbar.jsp" /> 
        
        <div class="container-fluid"> 
            
            <ol class="breadcrumb">
              <li><a href="${pageContext.request.contextPath}/gui/user/home">Home</a></li>
              <li><a href="${pageContext.request.contextPath}/gui/user/wms">Wms</a></li>
              <li class="active">${wmsEntity.name}</li>
            </ol>                
            
                <table class="table table-striped">                    
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Title</th>
                            <th>REST URL</th>
                            <th>Redirect URL</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${capabilities.getCapability().getLayerWrap().getLayer()}" var="capability">
                        <tr>
                            <td>${capability.getName()}</td>
                            <td>${capability.getTitle()}</td>
                            <td><a href="<c:url value="/api/wms/${wmsEntity.name}/getmap?layers=${capability.getName()}"/>">JSON</a></td>
                            <td><a href="<c:url value="/api/wms/${wmsEntity.name}/redirect?layers=${capability.getName()}"/>">Redirect</a></td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>                         
        </div>
    </body>
</html>
