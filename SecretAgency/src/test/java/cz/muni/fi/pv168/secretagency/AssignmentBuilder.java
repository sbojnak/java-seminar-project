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
    public AssignmentBuilder id(Long id){
        this.id = id;
        return this;
    }

    AssignmentBuilder mission(Mission mission){
        this.mission = mission;
        return this;
    }

    AssignmentBuilder agent(Agent agent){
        this.agent = agent;
        return this;
    }


    /**
     * Creates new instance of {@link Assignment} with configured properties.
     *
     * @return new instance of {@link Assignment} with configured properties.
     */
    Assignment build(){
        Assignment assignment = new Assignment();
        assignment.setId(id);
        assignment.setMission(mission);
        assignment.setAgent(agent);
        return assignment;
    }



}
