    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
    
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
           <li <c:if test="${view == 'home'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/user/home"><span class="glyphicon glyphicon-home"></span> Home</a></li>
           <security:authorize access="hasRole('ROLE_SUPERADMIN')"><li <c:if test="${view == 'account'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/superadmin/account"><span class="glyphicon glyphicon-user"></span> Accounts</a></li></security:authorize>            
           <security:authorize access="hasAnyRole('ROLE_SUPERADMIN')"><li <c:if test="${view == 'config'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/superadmin/config"><span class="glyphicon glyphicon-wrench"></span> Configs</a></li></security:authorize>    
           <security:authorize access="hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')"><li <c:if test="${view == 'log'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/admin/log"><span class="glyphicon glyphicon-flag"></span> Logs</a></li></security:authorize>                                 
           <security:authorize access="hasAnyRole('ROLE_SUPERADMIN','ROLE_ADMIN')"><li <c:if test="${view == 'log'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/admin/statistics"><span class="glyphicon glyphicon-stats"></span> Stats</a></li></security:authorize>   
         </ul>

         <ul class="nav navbar-nav navbar-right">
            <security:authorize access="hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPERADMIN')"><li <c:if test="${view == 'user'}">class="active"</c:if>><a href="${pageContext.request.contextPath}/gui/user"><span class="glyphicon glyphicon-cog"></span> ${pageContext.request.userPrincipal.name}</a></li></security:authorize>                        
            <li>
                <a href="javascript:formSubmit();"><span class="glyphicon glyphicon-off"></span> Logout</a>
                    <c:url value="/j_spring_security_logout" var="logoutUrl" />
                    <form action="${logoutUrl}" method="post" id="logoutForm">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    </form>
                    <script>
                        function formSubmit() 
                        {
                            document.getElementById("logoutForm").submit();
                        }
                    </script>
            </li>            
         </ul>
       </div>                                      
     </div>
    </nav>                       