<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css"/>
        <script type="text/javascript" async
                src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-MML-AM_CHTML">
        </script>
        <meta name="viewport" content="width=device-width,user-scalable=no,maximum-scale=1"/>
        <title>Web-Optimize</title>
    </head>
    <body>
        <h1>Web-Optimize</h1>
        <h3>線形計画問題を設定してください</h3>
        <%
            int n = 0;
            int m = 0;

            try {
                n = Integer.parseInt(request.getParameter("n"));
                m = Integer.parseInt(request.getParameter("m"));
            } catch (NumberFormatException ex) {
                request.setAttribute("ex", ex);
                request.getRequestDispatcher("index.html").include(request, response);
            }
        %>
        <form method="GET" action="CalculationServlet">
            <p>
                <select name="type">
                    <option value="maximize">MAXIMIZE</option>
                    <option value="minimize">MINIMIZE</option>
                </select>
            </p>
            <p>
                <% for (int j = 0; j < n; j++) { %>
                <% if (j != 0) { %>
                +
                <% }%>
                <input type="text" name="cost<%=j%>" class="text">\(x_{<%=j + 1%>}\)
                <% } %>
            </p>
            <p>SUBJECT TO</p>
            <p>
                <% for (int i = 0; i < m; i++) { %>
                <% for (int j = 0; j < n; j++) { %>
                <% if (j != 0) { %>
                +
                <% }%>
                <input type="text" name="coeff<%=i%><%=j%>" class="text">\(x_{<%=j + 1%>}\)
                <% }%>
                <select name="symbol<%=i%>">
                    <option value="=">=</option>
                    <option value="<=" selected><=</option>
                    <option value=">=">>=</option>
                </select>
                <input type="text" name="const<%=i%>" class="text">
                <br>
                <% } %>
            </p>
            <p>BOUND</p>
            <p>
                <% for (int j = 0; j < n; j++) {%>
                <input type="checkbox" name="isNonnegative" value=<%=j%> checked>
                \(x_{<%=j + 1%>} \geq 0\)
                <br>
                <% }%>
            </p>
            <p>
                <input type="submit" value="START!" class="button">
            </p>
            <% if (request.getAttribute("ex") != null) {%>
            <p class="error">
                数値を入力してください
            </p>
            <% }%>
        </form>
    </body>
</html>
