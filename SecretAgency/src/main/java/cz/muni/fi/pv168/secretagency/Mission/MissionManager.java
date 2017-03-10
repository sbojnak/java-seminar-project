package cz.muni.fi.pv168.secretagency.Mission;

import java.util.List;

public interface MissionManager {

	/**
	 * 
	 * @param id
	 */
	Mission findMissionById(Long id);

	/**
	 * 
	 * @param mission
	 */
	void createMission(Mission mission);

	/**
	 * 
	 * @param mission
	 */
	void editMission(Mission mission);

	/**
	 * 
	 * @param id
	 */
	void deleteMission(Long id);

	List<Mission> listMissions();

}