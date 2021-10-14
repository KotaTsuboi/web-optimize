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
        <h3>Click to Create Points</h3>

        <div id="myDiv" style="align: center; width:100%; height: 100%;">
            <!-- Plotly chart will be drawn here -->
        </div>
        <a onclick="execGet('TSPServlet', getData());return false;" href="#" class="button">START</a>
        <script>
            var myPlot = document.getElementById('myDiv');
            data = [];

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

            Plotly.plot('myDiv', data, layout)
                    .then(attach);

            function attach() {
                var xaxis = myPlot._fullLayout.xaxis;
                var yaxis = myPlot._fullLayout.yaxis;
                var l = myPlot._fullLayout.margin.l;
                var t = myPlot._fullLayout.margin.t;

                myPlot.addEventListener('click', function (evt) {
                    var xInDataCoord = xaxis.p2c(evt.layerX - l);
                    var yInDataCoord = yaxis.p2c(evt.layerY - t);

                    var point = {
                        x: [xInDataCoord],
                        y: [yInDataCoord],
                        type: 'scatter',
                        mode: 'markers',
                        marker: {size: 20},
                        name: 'point'
                    }

                    data.push(point);
                    Plotly.redraw('myDiv');
                });
            }

            function getData() {
                var xList = data.map(function (value, index, data) {
                    return value.x;
                });
                var yList = data.map(function (value, index, data) {
                    return value.y;
                });
                return {x: xList, y: yList};
            }

            function execGet(action, data) {
                var form = document.createElement("form");
                form.setAttribute("action", action);
                form.setAttribute("method", "get");
                form.style.display = "none";
                document.body.appendChild(form);
                if (data !== undefined) {
                    for (var paramName in data) {
                        var input = document.createElement('input');
                        input.setAttribute('type', 'hidden');
                        input.setAttribute('name', paramName);
                        input.setAttribute('value', data[paramName]);
                        form.appendChild(input);
                    }
                }
                form.submit();
            }

        </script>
    </body>
</html>
