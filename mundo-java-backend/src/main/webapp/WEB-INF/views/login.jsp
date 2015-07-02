<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">        
        <title>MUNDO JAVA BACKEND</title>
        <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/sb-admin.css" />" rel="stylesheet">
        <!-- Placed at the end of the document so the pages load faster -->                            
        <script src="<c:url value="/resources/js/jquery-1.11.1.min.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
        <script src="<c:url value="/resources/js/angular.min.js" />"></script>
        <script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.11.0.min.js" />"></script>     
    </head>

    <body>
        
    <center>
        <div id="titreaccueil"><h4 id='titreaccueil'>MUNDO BACKEND</h2></div>        
    </center>    
        
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Sign In</h3>
                    </div>
                    <div class="panel-body">
                        
                        <c:if test="${not empty error}">
                        <div class="error">${error}</div>
                        </c:if>
                        
                        <c:if test="${not empty msg}">
                        <div class="msg">${msg}</div>
                        </c:if>                        
                        
                        <form role="form" name="loginForm" action="<c:url value='j_spring_security_check' />" method="POST">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="username" ng-model="phoneNumber" name="username" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="password" name="password" type="password" value="">
                                </div>  
                                <input name="submit" class="btn btn-lg btn-warning btn-block" type="submit" value="Sign in" style="background-color: #FE6400; border-color: #EEA236; color: #FFFFFF;" />
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <footer></footer>
                            </fieldset>                            
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>                                 
                            
    </body>
</html>