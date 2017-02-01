package com.dynocryptlabs.Dash.Database.Models;

import com.dynocryptlabs.Dash.Database.ConnectionFactory;
import com.dynocryptlabs.Dash.Database.Utilities.PasswordStorage;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.LruObjectCache;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by rohanpanchal on 1/11/17.
 *
 * Model for User records in Database.
 */
@DatabaseTable(tableName = "users")
public class User {

    private static Dao<User, Integer> userDAO;

    public static final String USER_FIELD_ID = "id";
    public static final String USER_FIELD_FIRST_NAME = "first_name";
    public static final String USER_FIELD_LAST_NAME = "last_name";
    public static final String USER_FIELD_EMAIL = "email";
    public static final String USER_FIELD_PASSWORD_HASH = "password_hash";
    public static final String USER_FIELD_ACCESS_TOKEN = "access_token";
    public static final String USER_FIELD_IS_ACTIVE = "is_active";
    public static final String USER_FIELD_IS_ADMIN = "is_admin";
    public static final String USER_FIELD_CREATED_AT = "created_at";
    public static final String USER_FIELD_UPDATED_AT = "updated_at";

    @DatabaseField(generatedId = true, columnName = USER_FIELD_ID)
    private Integer id;

    @DatabaseField(columnName = USER_FIELD_FIRST_NAME)
    private String firstName;

    @DatabaseField(columnName = USER_FIELD_LAST_NAME)
    private String lastName;

    @DatabaseField(columnName = USER_FIELD_EMAIL)
    private String email;

    @DatabaseField(columnName = USER_FIELD_PASSWORD_HASH)
    private String passwordHash;

    @DatabaseField(columnName = USER_FIELD_ACCESS_TOKEN)
    private String accessToken;

    @DatabaseField(columnName = USER_FIELD_IS_ACTIVE, canBeNull = false, defaultValue = "true")
    private Boolean active;

    @DatabaseField(columnName = USER_FIELD_IS_ADMIN, canBeNull = false, defaultValue = "false")
    private Boolean admin;

    @DatabaseField(columnName = USER_FIELD_CREATED_AT, canBeNull = false, defaultValue = "now()")
    private Date createdAt;

    @DatabaseField(columnName = USER_FIELD_UPDATED_AT, canBeNull = false, defaultValue = "now()")
    private Date updatedAt;

    //================================================================================
    // Constructors
    //================================================================================

    private User() {}

    //================================================================================
    // DAO
    //================================================================================

    private static synchronized Dao<User, Integer> getUserDAO() {
        if (User.userDAO == null) {
            try {
                User.userDAO = DaoManager.createDao(ConnectionFactory.getConnectionSource(), User.class);
                User.userDAO.setObjectCache(new LruObjectCache(100));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return User.userDAO;
    }

    //================================================================================
    // Special Operations
    //================================================================================

    public static User registerUser(String email, String password, String firstName, String lastName, Boolean admin) throws SQLException {

        UserTeam userTeam = TransactionManager.callInTransaction(ConnectionFactory.getConnectionSource(), new Callable<UserTeam>() {
            @Override
            public UserTeam call() throws Exception {
                User user = build(email, firstName, lastName, password, admin);
                Team team = Team.build(firstName + " " + lastName);

                UserTeam userTeam = UserTeam.build(user, team, true);

                return userTeam;
            }
        });

        return userTeam.getUser();
    }

    public static User findFromAccessToken(String accessToken) throws SQLException {
        return User.getUserDAO()
                .queryBuilder()
                .where()
                .eq(USER_FIELD_ACCESS_TOKEN, accessToken)
                .queryForFirst();
    }

    //================================================================================
    // Operations
    //================================================================================

    public static User build(String email, String firstName, String lastName, String password, Boolean admin) throws SQLException {
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setAdmin(admin);
        user.generateAccessToken();

        return create(user);
    }

    public static User create(User user) throws SQLException {
        user.id = User.getUserDAO().create(user);
        return user;
    }

    public static User read(Integer id) throws SQLException {
        return User.getUserDAO().queryForId(id);
    }

    public static User read(String email) throws SQLException {
        return User.getUserDAO().queryBuilder().where().eq(USER_FIELD_EMAIL, email).queryForFirst();
    }

    public static List<User> read(Long page, Long size) throws SQLException {
        return User.getUserDAO().queryBuilder().offset(page).limit(size).query();
    }

    //================================================================================
    // Instance Operations
    //================================================================================

    public List<UserTeam> getUserTeams() throws SQLException {
        return UserTeam.getUserTeamDAO().queryBuilder().where().eq(UserTeam.USER_TEAM_FIELD_USER_ID, this.getId()).query();
    }

    public List<Team> getTeams() throws SQLException {
        QueryBuilder<UserTeam, Integer> userTeamQueryBuilder = UserTeam.getUserTeamDAO().queryBuilder();
        userTeamQueryBuilder.selectColumns(UserTeam.USER_TEAM_FIELD_TEAM_ID);
        userTeamQueryBuilder.where().eq(UserTeam.USER_TEAM_FIELD_USER_ID, this.getId());

        QueryBuilder<Team, Integer> teamQueryBuilder = Team.getTeamDAO().queryBuilder();
        teamQueryBuilder.where().in(Team.TEAM_FIELD_ID, userTeamQueryBuilder);

        return Team.getTeamDAO().query(teamQueryBuilder.prepare());
    }

    public Boolean update() throws SQLException {
        this.updatedAt = new Date();
        User.getUserDAO().update(this);
        return true;
    }

    //================================================================================
    // Instance Methods
    //================================================================================

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        try {
            this.passwordHash = PasswordStorage.createHash(password);
        } catch (PasswordStorage.CannotPerformOperationException exception) {
            return;
        }
    }

    public boolean validatePassword(String password) {
        try {
            return PasswordStorage.verifyPassword(password, this.passwordHash);
        } catch (Exception exception) {
            return false;
        }
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void generateAccessToken() {
        this.accessToken = new BigInteger(128, new SecureRandom()).toString();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
