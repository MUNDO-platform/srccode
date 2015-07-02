
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@page session="true"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>Api19115</title>                         
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
              <li><a href="${pageContext.request.contextPath}/gui/user/queue">Queues</a></li>
              <li class="active">${queueEntity.name}</li>
            </ol>              
            
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>IDGRUPY</th>
                        <th>LITERAGRUPY</th>
                        <th>NAZWAGRUPY</th>
                        <th>AKTUALNYNUMER</th>   
                        <th>LICZBAKLWKOLEJCE</th>
                        <th>LICZBACZYNNYCHSTAN</th>
                        <th>CZASOBSLUGI</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${groups}" var="group">
                    <tr>
                        <td>${group.idGrupy}</td>
                        <td>${group.literaGrupy}</td>
                        <td>${group.nazwaGrupy}</td>
                        <td>${group.aktualnyNumer}</td>
                        <td>${group.liczbaKlwKolejce}</td>
                        <td>${group.liczbaCzynnychStan}</td>
                        <td>${group.czasObslugi}</td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>          
    </body>
</html>