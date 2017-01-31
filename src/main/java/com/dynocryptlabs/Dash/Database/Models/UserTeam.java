package com.dynocryptlabs.Dash.Database.Models;

import com.dynocryptlabs.Dash.Database.ConnectionFactory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by rohanpanchal on 1/17/17.
 *
 * Model for UserTeam records in Database.
 */
@DatabaseTable(tableName = "user_teams")
public class UserTeam {

    private static Dao<UserTeam, Integer> userTeamDAO;

    public static final String USER_TEAM_FIELD_USER_ID = "user_id";
    public static final String USER_TEAM_FIELD_TEAM_ID = "team_id";
    public static final String USER_TEAM_FIELD_IS_ADMIN = "is_admin";
    public static final String USER_TEAM_FIELD_CREATED_AT = "created_at";
    public static final String USER_TEAM_FIELD_UPDATED_AT = "updated_at";

    @DatabaseField(foreign = true, columnName = USER_TEAM_FIELD_USER_ID, foreignColumnName = "id")
    private User user;

    @DatabaseField(foreign = true, columnName = USER_TEAM_FIELD_TEAM_ID, foreignColumnName = "id")
    private Team team;

    @DatabaseField(columnName = USER_TEAM_FIELD_IS_ADMIN, canBeNull = false, defaultValue = "false")
    private Boolean admin;


    @DatabaseField(columnName = USER_TEAM_FIELD_CREATED_AT, canBeNull = false, defaultValue = "now()")
    private Date createdAt;

    @DatabaseField(columnName = USER_TEAM_FIELD_UPDATED_AT, canBeNull = false, defaultValue = "now()")
    private Date updatedAt;

    //================================================================================
    // Constructors
    //================================================================================

    private UserTeam() {}

    //================================================================================
    // DAO
    //================================================================================

    public static synchronized Dao<UserTeam, Integer> getUserTeamDAO() {
        if (UserTeam.userTeamDAO == null) {
            try {
                UserTeam.userTeamDAO = DaoManager.createDao(ConnectionFactory.getConnectionSource(), UserTeam.class);
                UserTeam.userTeamDAO.setObjectCache(new LruObjectCache(100));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return UserTeam.userTeamDAO;
    }

    //================================================================================
    // Operations
    //================================================================================

    public static UserTeam build(User user, Team team, Boolean admin) throws SQLException {
        UserTeam userTeam = new UserTeam();
        userTeam.setUser(user);
        userTeam.setTeam(team);
        userTeam.setAdmin(admin);

        return create(userTeam);
    }

    public static UserTeam create(UserTeam userTeam) throws SQLException {
        UserTeam.getUserTeamDAO().create(userTeam);
        return userTeam;
    }

    public static UserTeam read(User user, Team team) throws SQLException {
        return UserTeam.getUserTeamDAO().queryBuilder().where()
                .eq(USER_TEAM_FIELD_USER_ID, user.getId())
                .eq(USER_TEAM_FIELD_TEAM_ID, team.getId())
                .queryForFirst();
    }

    //================================================================================
    // Instance Methods
    //================================================================================

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
