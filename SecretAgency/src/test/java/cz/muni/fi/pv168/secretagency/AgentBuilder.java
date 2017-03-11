package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.Agent;

import java.time.LocalDate;
import java.time.Month;


/**
 * Builder for {@link Agent} class on behalf of better test readability.
 *
 * Created by kubo on 10.3.2017.
 */
public class AgentBuilder {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private int securityLevel;

    public AgentBuilder id(Long id){
        this.id = id;
        return this;
    }

    public AgentBuilder name(String name){
        this.name = name;
        return this;
    }

    public AgentBuilder birthDate(LocalDate birthDate){
        this.birthDate = birthDate;
        return this;
    }

    public AgentBuilder birthDate(int year, Month month, int day){
        this.birthDate = LocalDate.of(year, month, day);
        return this;
    }

    public AgentBuilder securityLevel(int securityDegree){
        this.securityLevel = securityDegree;
        return this;
    }

    /**
     * Creates new instance of {@link Agent} with configured properties.
     *
     * @return new instance of {@link Agent} with configured properties.
     */
    public Agent build(){
        Agent agent = new Agent();
        agent.setId(id);
        agent.setName(name);
        agent.setBirthDate(birthDate);
        agent.setSecurityLevel(securityLevel);
        return agent;
    }
}
