<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width,maximum-scale=1"/>
        <title>Web Optimize</title>
        <link rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <h1><a href="<%= request.getContextPath()%>">Web Optimize</a></h1>
        <h3>Set Linear Programming Problem</h3>
        <form action="input.jsp" method="GET">
            <p>Number of variables: <input type="text" name="n" class="text"></p>
            <p>Number of constraints: <input type="text" name="m" class="text"></p>
            <p><input type="submit" value="CONFIRM" class="button"></p>
        </form>
    </body>
</html>
