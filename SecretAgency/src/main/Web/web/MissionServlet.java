package web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cz.muni.fi.pv168.secretagency.Mission.MissionManager;
import cz.muni.fi.pv168.secretagency.commons.ServiceFailureException;

import java.io.IOException;

/**
 * Created by pistab on 15.4.2017.
 */
@WebServlet(MissionServlet.URL_MAPPING + "/*")
public class MissionServlet extends HttpServlet {

    public static final String URL_MAPPING = "/missions";
    private static final String LIST_JSP = "/list.jsp";

    private MissionManager getMissionManager(){
        return (MissionManager)getServletContext().getAttribute("missionManager");
    }

    private void showMissions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setAttribute("missions", getMissionManager().listMissions());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        }
        catch(ServiceFailureException ex){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        showMissions(request, response);
    }
}
