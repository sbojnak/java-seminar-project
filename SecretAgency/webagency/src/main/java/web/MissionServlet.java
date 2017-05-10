package web;

import cz.muni.fi.pv168.secretagency.Mission.Mission;
import cz.muni.fi.pv168.secretagency.Mission.MissionManager;
import cz.muni.fi.pv168.secretagency.commons.ServiceFailureException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        switch(action){
            case "/add":
                String name = request.getParameter("name");
                String goal = request.getParameter("goal");
                String location = request.getParameter("location");
                String description = request.getParameter("description");

                if(name == null || name.length() == 0 || goal == null || goal.length() == 0 ||
                        location == null || location.length() == 0 || description == null || description.length() == 0){
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty !");
                    try {
                        showMissions(request, response);
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Mission mission = new Mission();
                mission.setName(name);
                mission.setGoal(goal);
                mission.setLocation(location);
                mission.setDescription(description);
                try{
                    getMissionManager().createMission(mission);
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                }
                catch(ServiceFailureException ex){
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/delete":
                Long id = Long.valueOf(request.getParameter("id"));
                Mission missionToDelete = new Mission();
                missionToDelete.setId(id);
                try{
                    getMissionManager().deleteMission(missionToDelete);
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                }
                catch(ServiceFailureException ex){
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/update":
                Long idOfUpdated = Long.valueOf(request.getParameter("id"));
                String updatedName = request.getParameter("name");
                String updatedGoal = request.getParameter("goal");
                String updatedLocation = request.getParameter("location");
                String updatedDescription = request.getParameter("description");

                Mission missionToUpdate = new Mission();
                missionToUpdate.setId(idOfUpdated);
                missionToUpdate.setName(updatedName);
                missionToUpdate.setGoal(updatedGoal);
                missionToUpdate.setLocation(updatedLocation);
                missionToUpdate.setDescription(updatedDescription);
                try{
                    getMissionManager().editMission(missionToUpdate);
                    response.sendRedirect(request.getContextPath() + URL_MAPPING);
                    return;
                }
                catch(ServiceFailureException ex){
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }
}
