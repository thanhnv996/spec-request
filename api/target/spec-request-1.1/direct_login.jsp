<%-- 
    Document   : login
    Created on : Jul 10, 2017, 8:34:40 AM
    Author     : vietduc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href='<%=request.getContextPath()%>/css/bootstrap.min.css'>
        <link rel="stylesheet" href='<%=request.getContextPath()%>/css/bootstrap-theme.min.css'>
        
        <title>Direct login</title>
    </head>
    <body>
        <div class="col-sm-8">

            <div class="page-header"><h3>Login</h3></div>
            <!-- FORM -->
            <div>
                <form target="" method="POST">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="login_id" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" class="form-control">
                    </div>
                    <input type="submit" class="btn btn-primary" value="Login">
                    <p>${login_result}</p>
                </form>
            </div>
        </div>
    </body>
</html>
