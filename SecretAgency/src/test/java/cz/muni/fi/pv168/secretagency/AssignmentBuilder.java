package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Mission.Mission;
import cz.muni.fi.pv168.secretagency.Assignment.Assignment;

/**
 *  Builder for {@link Assignment} class on behalf of better test readability.
 *
 * Created by Jakub Bartolomej Kosuth on 16.3.2017.
 */
public class AssignmentBuilder {

    private Long id;
    private Mission mission;
    private Agent agent;
    private Boolean jobCompleted;

    public AssignmentBuilder id(Long id){
        this.id = id;
        return this;
    }

    public AssignmentBuilder mission(Mission mission){
        this.mission = mission;
        return this;
    }

    public AssignmentBuilder agent(Agent agent){
        this.agent = agent;
        return this;
    }

    public AssignmentBuilder jobCompleted(boolean status){
        this.jobCompleted = status;
        return  this;
    }


    /**
     * Creates new instance of {@link Assignment} with configured properties.
     *
     * @return new instance of {@link Assignment} with configured properties.
     */
    public Assignment build(){
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setMission(mission);
        assignment.setAgent(agent);
        assignment.setJobCompleted(jobCompleted);
        return assignment;
    }



}
