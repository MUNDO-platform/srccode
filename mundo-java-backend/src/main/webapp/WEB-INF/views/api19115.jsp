
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
              <li><a href="${pageContext.request.contextPath}/gui/user/api19115">Api19115</a></li>
              <li class="active">${apiEntity.name}</li>
            </ol>              
            
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ApiFeature</th>
                        <th>Method</th>
                        <th>REST URL (example)</th>
                        <th>API request</th>   
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>getNotifications</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotifications?filters=${filterExample}&operators=${operatorExample}</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotifications?filters=${filterExample}&operators=${operatorExample}">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>getNotificationsForDate</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForDate?dateFrom=${dateExampleFrom}&dateTo=${dateExampleTo}</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForDate?dateFrom=${dateExampleFrom}&dateTo=${dateExampleTo}">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>getNotificationsForDistrict</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForDistrict?district=Wola</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForDistrict?district=Wola">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>getNotificationsForNotificationNumber</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForNotificationNumber?notificationNumber=123/45</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotificationsForNotificationNumber?notificationNumber=123/45">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>sendFreeform</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendFreeform?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendFreeform?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>sendIncident</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendIncident?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test&xCoordWGS84=54&yCoordWGS84=74&subcategory=other&eventType=ip_other_001&street=lipowa&houseNumber=1&apartmentNumber=1</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendIncident?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test&xCoordWGS84=54&yCoordWGS84=74&subcategory=other&eventType=ip_other_001&street=lipowa&houseNumber=1&apartmentNumber=1">JSON</a></td>
                    </tr>
                    <tr>    
                        <td>sendInformational</td>
                        <td>GET</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendInformational?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test</td>
                        <td><a target="_blank" href="${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendInformational?email=mudo@mundo.pl&name=test&lastName=test&phoneNumber=500100200&description=test">JSON</a></td>
                    </tr>

                     <tr>
                        <td>getNotifications</td>
                        <td>POST</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/getNotifications</td>
                        <td>-</td>
                    </tr>
                 
                    <tr>    
                        <td>sendFreeform</td>
                        <td>POST</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendFreeform</td>
                        <td>-</td>
                    </tr>
                    <tr>    
                        <td>sendIncident</td>
                        <td>POST</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendIncident</td>
                        <td>-</td>
                    </tr>
                    <tr>    
                        <td>sendInformational</td>
                        <td>POST</td>
                        <td>${pageContext.request.contextPath}/api/api19115/${apiEntity.name}/sendInformational</td>
                        <td>-</td>
                    </tr>
                </tbody>
            </table>
        </div>          
    </body>
</html>