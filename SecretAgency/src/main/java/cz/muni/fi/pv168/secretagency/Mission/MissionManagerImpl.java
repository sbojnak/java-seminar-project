package cz.muni.fi.pv168.secretagency.Mission;

import java.util.List;
import javax.sql.DataSource;

public class MissionManagerImpl implements MissionManager {

    private final DataSource dataSource;

    public MissionManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
	 * 
	 * @param id mission id
	 */
	public Mission findMissionById(Long id) {
		// TODO - implement MissionManagerImpl.findMissionById
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param mission mission to create
	 */
	public void createMission(Mission mission) {
		// TODO - implement MissionManagerImpl.createMission
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param mission mission to edit
	 */
	public void editMission(Mission mission) {
		// TODO - implement MissionManagerImpl.editMission
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param mission mission which will be deleted
	 */
	public void deleteMission(Mission mission) {
		// TODO - implement MissionManagerImpl.deleteMission
		throw new UnsupportedOperationException();
	}

	public List<Mission> listMissions() {
		// TODO - implement MissionManagerImpl.listMissions
		throw new UnsupportedOperationException();
	}

}