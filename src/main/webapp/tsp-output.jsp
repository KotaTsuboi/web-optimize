<%@page import="java.util.Arrays"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
        <title>Web Optimize</title>
        <link rel="stylesheet" href="style.css"/>
    </head>

    <body>
        <h1><a href="<%= request.getContextPath()%>">Web Optimize</a></h1>
        <h3>Traveling Route was Found</h3>

        <div id="myDiv" style="align: center; width:100%; height: 100%;">
            <!-- Plotly chart will be drawn here -->
        </div>
        <a href="<%= request.getContextPath()%>/tsp-input.jsp" class="button">BACK</a>
        <script>
            var url = new URL(window.location.href);
            var params = url.searchParams;
            var myPlot = document.getElementById('myDiv');
            data = [];
            var x = params.get("x").split(",");
            var y = params.get("y").split(",");
            var order = [
            <% int[] order = (int[]) request.getAttribute("order"); %>
            <% for (int i = 0; i < order.length; i++) {%>
            <% if (i != 0) {%>
            ,
            <% }%>
            <%= order[i]%>
            <% }%>
            ];
            for (var i = 0; i < order.length; i++) {
                var line = {
                    x: [x[order[i]], x[order[(i + 1) % order.length]]],
                    y: [y[order[i]], y[order[(i + 1) % order.length]]],
                    mode: 'scatters',
                    line: {
                        width: 2, color: 'red'
                    },
                    marker: {
                        size: 20
                    },
                    opacity: 1.0
                }

                data.push(line);
            }

            layout = {
                hovermode: 'closest',
                height: 1000,
                xaxis: {
                    range: [0, 1],
                    linecolor: 'black',
                    linewidth: 2,
                    mirror: true,
                    showgrid: false
                },
                yaxis: {
                    range: [0, 1],
                    linecolor: 'black',
                    linewidth: 2,
                    mirror: true,
                    showgrid: false,
                },
                showlegend: false
            };
            Plotly.plot('myDiv', data, layout);
        </script>
    </body>
</html>
