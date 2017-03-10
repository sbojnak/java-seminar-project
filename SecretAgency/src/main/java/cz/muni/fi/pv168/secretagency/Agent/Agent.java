package cz.muni.fi.pv168.secretagency.Agent;

import java.time.LocalDate;

public class Agent {

	private Long id;
	private String name;
	private LocalDate birthDate;
	private int securityDegree;

	public Agent() {
		// TODO - implement Agent.Agent
		throw new UnsupportedOperationException();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public void setSecurityDegree(int securityDegree) {
		this.securityDegree = securityDegree;
	}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getSecurityDegree() {
        return securityDegree;
    }
}