package jp.ac.u_tokyo.iis.space.web.optimize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import jp.ac.u_tokyo.iis.space.optimization.algorithm.SimplexMethod;
import jp.ac.u_tokyo.iis.space.optimization.constraint.EquationSymbol;
import jp.ac.u_tokyo.iis.space.optimization.constraint.LinearConstraint;
import jp.ac.u_tokyo.iis.space.optimization.constraint.LinearEquation;
import jp.ac.u_tokyo.iis.space.optimization.function.LinearFunction;
import jp.ac.u_tokyo.iis.space.optimization.problem.LinearProgrammingProblem;
import jp.ac.u_tokyo.iis.space.optimization.solution.AbstractContinuousSolution;

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

        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.contains("cost")) {
                n++;
            } else if (paramName.contains("const")) {
                m++;
            }
        }

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
        boolean[] nonnegative = new boolean[n];

        for (int j = 0; j < n; j++) {
            String[] isNonnegatives = request.getParameterValues("isNonnegative");
            for (String isNonnegative : isNonnegatives) {
                if (isNonnegative.equals(String.valueOf(j))) {
                    nonnegative[j] = true;
                }
            }
        }

        LinearConstraint linearConstraint = new LinearConstraint(linearEquations, nonnegative);
        LinearProgrammingProblem standardForm = new LinearProgrammingProblem(true, linearFunction, linearConstraint);
        SimplexMethod instance = new SimplexMethod(standardForm);
        AbstractContinuousSolution solution = instance.run();

        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CalculationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println(solution.toString());
            out.println("</body>");
            out.println("</html>");
        }
    }

}
