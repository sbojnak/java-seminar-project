package cz.muni.fi.pv168.secretagency.Assignment;

import cz.muni.fi.pv168.secretagency.Agent.Agent;
import cz.muni.fi.pv168.secretagency.Mission.Mission;

public class Assignment {

	private Long id;
	private Mission mission;
	private Agent agent;
	private Boolean jobCompleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Boolean getJobCompleted() {
		return jobCompleted;
	}

	public void setJobCompleted(Boolean jobCompleted) {
		this.jobCompleted = jobCompleted;
	}
}