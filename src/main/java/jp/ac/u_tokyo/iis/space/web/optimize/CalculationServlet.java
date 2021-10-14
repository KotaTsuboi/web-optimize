package jp.ac.u_tokyo.iis.space.web.optimize;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.ac.u_tokyo.iis.space.optimization.algorithm.SimplexMethod;
import jp.ac.u_tokyo.iis.space.optimization.boundary.NonnegativeCondition;
import jp.ac.u_tokyo.iis.space.optimization.constraint.LinearConstraint;
import jp.ac.u_tokyo.iis.space.optimization.equation.LinearEquation;
import jp.ac.u_tokyo.iis.space.optimization.exception.UnboundedException;
import jp.ac.u_tokyo.iis.space.optimization.exception.UnfeasibleException;
import jp.ac.u_tokyo.iis.space.optimization.function.LinearFunction;
import jp.ac.u_tokyo.iis.space.optimization.orientation.Orientation;
import jp.ac.u_tokyo.iis.space.optimization.problem.LinearProgrammingProblem;
import jp.ac.u_tokyo.iis.space.optimization.solution.ContinuousSolution;
import jp.ac.u_tokyo.iis.space.optimization.symbol.EquationSymbol;

/**
 *
 * @author Kota
 */
public class CalculationServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Enumeration<String> paramNames = request.getParameterNames();
        int n = 0;
        int m = 0;

        Orientation orientation;
        String type = request.getParameter("type");

        if (type.equals("maximize")) {
            orientation = Orientation.MAXIMIZE;
        } else if (type.equals("minimize")) {
            orientation = Orientation.MINIMIZE;
        } else {
            throw new IllegalArgumentException("Type " + type + " is undefined.");
        }

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.contains("cost")) {
                n++;
            } else if (paramName.contains("const")) {
                m++;
            }
        }

        try {
            double[] costs = new double[n];

            for (int j = 0; j < n; j++) {
                costs[j] = Double.parseDouble(request.getParameter("cost" + j));
            }

            LinearEquation[] equations = new LinearEquation[m];

            for (int i = 0; i < m; i++) {
                double[] coeff = new double[n];
                for (int j = 0; j < n; j++) {
                    coeff[j] = Double.parseDouble(request.getParameter(("coeff" + i) + j));
                }
                EquationSymbol symbol = EquationSymbol.parse(request.getParameter("symbol" + i));
                double constant = Double.parseDouble(request.getParameter("const" + i));
                equations[i] = new LinearEquation(coeff, symbol, constant);
            }

            LinearFunction linearFunction = new LinearFunction(costs);
            LinearEquation[] linearEquations = equations;
            String[] isNonnegatives = request.getParameterValues("isNonnegative");
            ArrayList<Integer> variableIndexList = new ArrayList<>();

            if (isNonnegatives != null) {
                for (int j = 0; j < n; j++) {
                    for (String isNonnegative : isNonnegatives) {
                        if (isNonnegative.equals(String.valueOf(j))) {
                            variableIndexList.add(j);
                        }
                    }
                }
            }

            int[] variableIndexes = new int[variableIndexList.size()];

            for (int i = 0; i < variableIndexList.size(); i++) {
                variableIndexes[i] = variableIndexList.get(i);
            }

            NonnegativeCondition boundary = new NonnegativeCondition(variableIndexes);

            LinearConstraint linearConstraint = new LinearConstraint(linearEquations);
            LinearProgrammingProblem standardForm = new LinearProgrammingProblem(orientation, linearFunction, linearConstraint, boundary);
            System.out.println((standardForm.toStandardForm().toString()));
            SimplexMethod instance = new SimplexMethod(standardForm);
            ContinuousSolution solution;

            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"style.css\"/>");
            out.println("<script type=\"text/javascript\" async");
            out.println("src=\"https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.1/MathJax.js?config=TeX-MML-AM_CHTML\">");
            out.println("</script>");
            out.println("<meta name=\"viewport\" content=\"width=device-width,user-scalable=no,maximum-scale=1\"/>");
            out.println("<title>Web Optimize</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1><a href=" + request.getContextPath() + ">Web Optimize</a></h1>");
            try {
                solution = instance.solve();
                out.println("<h3>🎉Optimal Solution was Found🎉</h3>");
                out.println("<br>");

                for (int i = 0; i < solution.size(); i++) {
                    out.println("\\(");
                    out.println("x_{" + (i + 1) + "} = " + solution.getValue(i));
                    out.println("\\)");
                    out.println("<br>");
                }
            } catch (UnboundedException ex) {
                out.println("<h3>🚨️Unbounded Problem🚨️</h3>");
            } catch (UnfeasibleException ex) {
                out.println("<h3>🚨️Origin Point is Unfeasible🚨<br>Two-phase Simplex Method is not Supported</h3>");
            } finally {
                out.println("</body>");
                out.println("</html>");
                out.close();
            }

        } catch (IllegalArgumentException ex) {
            request.setAttribute("ex", ex);
            request.getRequestDispatcher("input.jsp").include(request, response);
        }
    }

}
