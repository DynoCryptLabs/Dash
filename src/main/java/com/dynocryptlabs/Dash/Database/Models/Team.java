package com.dynocryptlabs.Dash.Database.Models;

import com.dynocryptlabs.Dash.Database.ConnectionFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by rohanpanchal on 1/17/17.
 *
 * Model for Team records in Database.
 */
@DatabaseTable(tableName = "teams")
public class Team {

    private static Dao<Team, Integer> teamDAO;

    public static final String TEAM_FIELD_ID = "id";
    public static final String TEAM_FIELD_NAME = "name";

    @DatabaseField(generatedId = true, columnName = TEAM_FIELD_ID)
    private Integer id;

    @DatabaseField(columnName = TEAM_FIELD_NAME)
    private String name;

    //================================================================================
    // Constructors
    //================================================================================

    public Team() {}

    //================================================================================
    // DAO
    //================================================================================

    public static synchronized Dao<Team, Integer> getTeamDAO() {
        if (Team.teamDAO == null) {
            try {
                Team.teamDAO = DaoManager.createDao(ConnectionFactory.getConnectionSource(), Team.class);
                Team.teamDAO.setObjectCache(new LruObjectCache(100));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Team.teamDAO;
    }

    //================================================================================
    // Operations
    //================================================================================

    public static Team build(String name) throws SQLException {
        Team team = new Team();
        team.setName(name);

        return create(team);
    }

    public static Team create(Team team) throws SQLException {
        team.id = Team.getTeamDAO().create(team);
        return team;
    }

    public static Team read(Integer id) throws SQLException {
        return Team.getTeamDAO().queryBuilder().where().eq(TEAM_FIELD_ID, id).queryForFirst();
    }

    //================================================================================
    // Instance Methods
    //================================================================================

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
