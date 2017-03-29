package cz.muni.fi.pv168.secretagency;

import cz.muni.fi.pv168.secretagency.Agent.AgentManagerImpl;
import cz.muni.fi.pv168.secretagency.Mission.Mission;
import cz.muni.fi.pv168.secretagency.Mission.MissionManagerImpl;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import javax.xml.bind.ValidationException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;



/**
 * Tests for MissionManager
 *
 * @author Stefan Bojnak
 * @version 2017-03-16
 */
public class MissionManagerImplTest {

    private MissionManagerImpl missionManager;
    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        dataSource = prepareDataSource();
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("CREATE TABLE MISSION ("
                    + "id bigint primary key generated always as identity,"
                    + "name varchar(50) not null,"
                    + "goal varchar(50) not null,"
                    + "location varchar(50) not null,"
                    + "descriptions varchar(255) not null)").executeUpdate();
        }
        missionManager = new MissionManagerImpl(dataSource);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.prepareStatement("DROP TABLE MISSION").executeUpdate();
        }
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:secretAgencyMission-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();



    private MissionBuilder goToShopMissionBuilder() {
        return new MissionBuilder().id(null)
                .description("This is a simple mission: Go to shop and buy me an apple")
                .goal("Get an apple for me")
                .name("Apple mission")
                .location("Brno");
    }

    private MissionBuilder killTheTerroristMissionBuilder() {
        return new MissionBuilder().id(null)
                .description("Very difficult mission, be careful")
                .goal("Find and kill the terrorist")
                .name("Terrorist mission")
                .location("Khazad dum");
    }

    private MissionBuilder nullGoalMissionBuilder() {
        return new MissionBuilder().id(null)
                .description("This is mission with null goal")
                .goal(null)
                .name("Null goal mission")
                .location("Mordor");
    }

    private MissionBuilder tooLongNameMissionBuilder() {
        return new MissionBuilder().id(null)
                .description("This mission has longer name than it should have")
                .goal("Long name")
                .name("This is longer name of mission that is really allowed, " +
                        "it means validation exception should be thrown and it will be tested")
                .location("Angmar");
    }

    @Test
    public void createMissionIdNotNull() {
        Mission mission = goToShopMissionBuilder().build();
        missionManager.createMission(mission);
        assertThat(mission.getId()).isNotNull();
    }

    @Test
    public void createMissionSavedSameFields() {
        Mission mission = goToShopMissionBuilder().build();
        missionManager.createMission(mission);
        Long missionId = mission.getId();
        assertThat(missionManager.findMissionById(missionId)).isEqualToComparingFieldByField(mission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullMission() {
        missionManager.createMission(null);
    }

    @Test(expected = SQLException.class)
    public void createMissionWithNUllGoal() {
        nullGoalMissionBuilder().build();
    }

    @Test(expected = ValidationException.class)
    public void createMissionWithTooLongName() {
        tooLongNameMissionBuilder().build();
    }

    @FunctionalInterface
    private interface Operation<T> {
        void callOn(T subjectOfOperation);
    }

    private void editMissionOperation(Operation<Mission> operation) {
        Mission mission = goToShopMissionBuilder().build();
        Mission secondMission = killTheTerroristMissionBuilder().build();
        missionManager.createMission(mission);
        missionManager.createMission(secondMission);
        operation.callOn(mission);
        missionManager.editMission(mission);
        assertThat(missionManager.findMissionById(mission.getId()))
                .isEqualToComparingFieldByField(mission);
        assertThat(missionManager.findMissionById(secondMission.getId()))
                .isEqualToComparingFieldByField(secondMission);
    }

    @Test
    public void editMissionDescription() {
        editMissionOperation((mission) -> mission.setDescription("Updated description"));
    }

    @Test
    public void editMissionGoal() {
        editMissionOperation((mission) -> mission.setGoal("Updated goal"));
    }

    @Test
    public void editMissionLocation() {
        editMissionOperation((mission) -> mission.setLocation("Updated location"));
    }

    @Test
    public void editMissionName() {
        editMissionOperation((mission) -> mission.setName("Updated name"));
    }

    @Test(expected = SQLException.class)
    public void editMissionWithNullGoal() {
        editMissionOperation((mission) -> mission.setGoal(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void editNullMission() {
        missionManager.editMission(null);
    }

    @Test
    public void editMissionWithTooLongName() {
        Mission mission = goToShopMissionBuilder().build();
        missionManager.createMission(mission);
        mission.setName("This is longer name of mission that is really allowed, " +
                "it means validation exception should be thrown and it will be tested");
        expectedException.expect(ValidationException.class);
        missionManager.editMission(mission);
    }

    @Test
    public void deleteMission() {
        Mission mission = goToShopMissionBuilder().build();
        Mission secondMission = killTheTerroristMissionBuilder().build();
        missionManager.createMission(mission);
        missionManager.createMission(secondMission);
        missionManager.deleteMission(mission);
        assertThat(missionManager.findMissionById(mission.getId())).isNull();
        assertThat(missionManager.findMissionById(secondMission.getId())).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNullMission(){
        missionManager.editMission(null);
    }

    @Test
    public void listMissions(){
        Mission shopMission = goToShopMissionBuilder().build();
        Mission terroristMission = killTheTerroristMissionBuilder().build();
        missionManager.createMission(shopMission);
        missionManager.createMission(terroristMission);
        //change to containsonly, when finding out, how to clear database
        assertThat(missionManager.listMissions()).usingFieldByFieldElementComparator()
                .contains(shopMission, terroristMission);
    }
}
