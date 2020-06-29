package ru.ifmo.lab.database;
import ru.ifmo.lab.object.Organization;
import ru.ifmo.lab.object.OrganizationType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CollectionDBManager {

    private final Connection connection;

    public CollectionDBManager(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Organization> getCollection() throws SQLException {
        ArrayList<Organization> collection = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.ORGANIZATION);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();
            Organization organization = new Organization(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("x"),
                    rs.getDouble("y"),
                    creationDate,
                    rs.getInt("annual_turnover"),
                    rs.getString("full_name"),
                    rs.getLong("employees_count"),
                    OrganizationType.getById(rs.getInt("type")-1),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    rs.getInt("user_id"),
                    rs.getString("username")

            );

            collection.add(organization);
        }

        return collection;
    }

    public boolean hasPermissions(Credentials credentials, int organizationID) throws SQLException {
        if (credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return true;

        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.USER_HAS_PERMISSIONS);
        int pointer = 0;
        preparedStatement.setInt(1, credentials.id);
        preparedStatement.setInt(2, organizationID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("exists");
        }
        return false;
    }

    public String add(Organization organization, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.ORGANIZATION);
            preparedStatement.setString(1, organization.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(organization.getCreationDate().atStartOfDay()));
            preparedStatement.setInt(3, organization.getAnnualTurnover());
            preparedStatement.setString(4, organization.getFullName());
            preparedStatement.setLong(5, organization.getEmployeesCount());
            preparedStatement.setInt(6, organization.getType().getId()+1);
            preparedStatement.setString(7, organization.getPostalAddress().getStreet());
            preparedStatement.setString(8, organization.getPostalAddress().getZipCode());
            preparedStatement.setInt(9, credentials.id);
            preparedStatement.setString(10, credentials.username);

            ResultSet rs = preparedStatement.executeQuery();
            int organizationID = 0;
            if (rs.next())
                organizationID = rs.getInt(1);

            preparedStatement = connection.prepareStatement(SqlQuery.Add.COORDINATE);
            preparedStatement.setFloat(1, organization.getCoordinates().getX());
            preparedStatement.setDouble(2, organization.getCoordinates().getY());
            preparedStatement.setInt(3, organizationID);
            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement(SqlQuery.Add.ORGANIZATION_USER_RELATIONSHIP);
            preparedStatement.setInt(1, credentials.id);
            preparedStatement.setInt(2, organizationID);
            preparedStatement.executeUpdate();

            connection.commit();

            return String.valueOf(organizationID);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String update(int id, Organization organization, Credentials credentials) throws SQLException {
        if (!hasPermissions(credentials, id))
            return "You don't have permissions";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Update.ORGANIZATION);
            preparedStatement.setString(1, organization.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(organization.getCreationDate().atStartOfDay()));
            preparedStatement.setInt(3, organization.getAnnualTurnover());
            preparedStatement.setString(4, organization.getFullName());
            preparedStatement.setLong(5, organization.getEmployeesCount());
            preparedStatement.setInt(6, organization.getType().getId());
            preparedStatement.setString(7, organization.getPostalAddress().getStreet());
            preparedStatement.setString(8, organization.getPostalAddress().getZipCode());
            preparedStatement.setInt(9, credentials.id);
            preparedStatement.setString(10, credentials.username);
            preparedStatement.setInt(11, id);
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Update.COORDINATE);
            preparedStatement.setFloat(1, organization.getCoordinates().getX());
            preparedStatement.setDouble(2, organization.getCoordinates().getY());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();


            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public int getOrganizationByID(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.ORGANIZATION_BY_ID);
        int pointer = 0;
        preparedStatement.setInt(++pointer, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public String deleteAll(Credentials credentials) throws SQLException {
        if (!credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return "You don't have permissions to delete all ru.ifmo.lab.database, only root";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ALL_ORGANIZATION);
            preparedStatement.executeUpdate();
            connection.commit();
            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String delete(int id, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            int dragonID = getOrganizationByID(id);
            if (!hasPermissions(credentials, dragonID))
                return "You don't have permissions";

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ORGANIZATION_BY_ID);
            int pointer = 0;
            preparedStatement.setInt(++pointer, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

}
