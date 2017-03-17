package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Mission.Mission;

/**
 * Created by pistab on 16.3.2017.
 */
public class MissionBuilder {
    private Long id;
    private String name;
    private String goal;
    private String location;
    private String description;

    public Mission build(){
        Mission mission = new Mission();
        mission.setId(id);
        mission.setName(name);
        mission.setGoal(goal);
        mission.setLocation(location);
        mission.setDescription(description);
        return mission;
    }

    public MissionBuilder id(Long id){
        this.id = id;
        return this;
    }

    public MissionBuilder name(String name){
        this.name = name;
        return this;
    }

    public MissionBuilder goal(String goal){
        this.goal = goal;
        return this;
    }

    public MissionBuilder location(String location){
        this.location = location;
        return this;
    }

    public MissionBuilder description(String description){
        this.description = description;
        return this;
    }
}
