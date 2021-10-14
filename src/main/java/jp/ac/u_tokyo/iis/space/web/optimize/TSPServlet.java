package jp.ac.u_tokyo.iis.space.web.optimize;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.ac.u_tokyo.iis.space.optimization.algorithm.TSPSolver;
import jp.ac.u_tokyo.iis.space.optimization.function.DistanceCalculator;
import jp.ac.u_tokyo.iis.space.optimization.problem.TravelingSalesmanProblem;
import jp.ac.u_tokyo.iis.space.optimization.solution.PermutationSolution;

/**
 *
 * @author Kota
 */
public class TSPServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] x = request.getParameter("x").split(",");
        String[] y = request.getParameter("y").split(",");
        final int N = x.length;
        double[][] xyList = new double[N][2];

        for (int i = 0; i < N; i++) {
            xyList[i][0] = Double.parseDouble(x[i]);
            xyList[i][1] = Double.parseDouble(y[i]);
        }

        DistanceCalculator function = new DistanceCalculator(xyList);
        TravelingSalesmanProblem tsp = new TravelingSalesmanProblem(function);
        int[] order = new int[N];

        for (int i = 0; i < N; i++) {
            order[i] = i;
        }

        PermutationSolution initialSolution = new PermutationSolution(order);
        TSPSolver solver = new TSPSolver(tsp, initialSolution);
        PermutationSolution optimalSolution = solver.solve();

        for (int i = 0; i < N; i++) {
            order[i] = optimalSolution.getOrder(i);
        }

        request.setAttribute("order", order);
        String destination = "tsp-output.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
        dispatcher.forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
