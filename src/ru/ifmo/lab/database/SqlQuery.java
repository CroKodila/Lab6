package ru.ifmo.lab.database;

public class SqlQuery {
    public static class Get {
        //Organization
        public static final String ORGANIZATION = "SELECT organization.id, organization.name, coordinates.x, coordinates.y, organization.creation_date, organization.annual_turnover, organization.full_name, organization.employees_count, organization.type, organization.street, organization.zip_code, organization.user_id, organization.username\n" +
                "FROM organization\n" +
                "    INNER JOIN coordinates ON organization.id = coordinates.organization_id";

        public static final String ORGANIZATION_BY_ID = "SELECT id FROM organization where id = ?";

        //Users
        public static final String USERS = "SELECT * FROM users";
        public static final String PASS_USING_USERNAME = "SELECT password, id FROM users WHERE username = ?";
        public static final String ID_USING_USERNAME = "SELECT id FROM users WHERE username = ?";

        public static final String USER_HAS_PERMISSIONS = "" +
                "SELECT exists(SELECT 1 from users_organization where user_id = ? AND organization_id = ?)";
    }
    public static class Add {
        //Organization
        public static final String ORGANIZATION = "" +
                "INSERT INTO organization (name, creation_date, annual_turnover, full_name, employees_count, type, street, zip_code, user_id, username)\n"+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        public static final String COORDINATE = "" +
                "INSERT INTO coordinates(x, y, organization_id) " +
                "VALUES(?, ?, ?)";


        //Users
        public static final String USER = "" +
                "INSERT INTO users(username, password) VALUES(?, ?)";

        public static final String ORGANIZATION_USER_RELATIONSHIP = "" +
                "INSERT INTO users_organization VALUES (?, ?)";
    }
    public static class Update {
        public static final String ORGANIZATION = "" +
        "UPDATE organization SET name=?, creation_date=?, annual_turnover=?, full_name=?, employees_count=?, type=?, street=?, zip_code=?, user_id=?, username=?\n"+
                "WHERE organization.id = ?;";
        public static final String COORDINATE = "" +
                "UPDATE coordinates SET x = ?, y = ? WHERE organization_id = ?";
    }
    public static class Delete {
        public static final String ALL_ORGANIZATION = "DELETE FROM organization";
        public static final String ORGANIZATION_BY_ID = "DELETE FROM organization where id = ?";

        public static final String USER = "DELETE FROM users where username = ?";
    }
}
