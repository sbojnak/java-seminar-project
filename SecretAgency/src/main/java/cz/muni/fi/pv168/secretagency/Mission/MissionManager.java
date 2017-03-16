package cz.muni.fi.pv168.secretagency.Mission;

import java.util.List;

public interface MissionManager {

	/**
	 * 
	 * @param id id of mission
	 */
	Mission findMissionById(Long id);

	/**
	 * 
	 * @param mission mission to create
	 */
	void createMission(Mission mission);

	/**
	 * 
	 * @param mission mission to edit
	 */
	void editMission(Mission mission);

	/**
	 * 
	 * @param mission mission which will be deleted
	 */
	void deleteMission(Mission mission);

	List<Mission> listMissions();

}