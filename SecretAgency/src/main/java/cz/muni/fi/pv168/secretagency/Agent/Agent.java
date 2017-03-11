package cz.muni.fi.pv168.secretagency.Agent;

import java.time.LocalDate;

public class Agent {

	private Long id;
	private String name;
	private LocalDate birthDate;
	private int securityLevel;

	public Agent() {

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

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
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

    public int getSecurityLevel() {
        return securityLevel;
    }
}