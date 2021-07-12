<%-- 
    Document   : input
    Created on : 2021/07/09, 2:13:43
    Author     : Kota
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>線形計画問題を設定してください。</h1>
        <%
            int n = Integer.parseInt(request.getParameter("n"));
            int m = Integer.parseInt(request.getParameter("m"));
        %>
        <form method="GET" action="CalculationServlet">
            <select name="type">
                <option value="maximize">maximize</option>
                <option value="maximize">minimize</option>
            </select>
            <br>
            <% for (int j = 0; j < n; j++) { %>
            <% if (j != 0) { %>
            +
            <% } %>
            <input type="text" name="cost<%=j%>">X<%=j + 1%>
            <% } %>
            <br>
            subject to
            <br>
            <% for (int i = 0; i < m; i++) { %>
            <% for (int j = 0; j < n; j++) { %>
            <% if (j != 0) { %>
            +
            <% } %>
            <input type="text" name="coeff<%=i%><%=j%>">X<%=j + 1%>
            <% } %>
            <select name="symbol<%=i%>">
                <option value="=">=</option>
                <option value="<="><=</option>
                <option value=">=">>=</option>
            </select>
            <input type="text" name="const<%=i%>">
            <br>
            <% } %>
            <% for (int j = 0; j < n; j++) { %>
            <input type="checkbox" name="isNonnegative" value=<%=j%>>
            <% } %>
            <input type="submit" value="計算開始">
        </form>
    </body>
</html>
