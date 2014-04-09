package net.piotrl.mvcrud.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Methods to control database
 * Building SQL queries.
 */
class Dbal {

    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;

    private static Connection connect;
    private static Statement request;

    static {
        Properties props = new Properties();
        FileInputStream in = null;

        try {
            in = new FileInputStream(ClassLoader.getSystemResource("database.properties").getPath());
            props.load(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String dbDrivers = props.getProperty("jdbc.drivers");
        if(dbDrivers != null) {
            System.setProperty("jdbc.drivers", dbDrivers);
        }

        dbUser = props.getProperty("jdbc.username");
        dbPassword = props.getProperty("jdbc.password");
        dbUrl = props.getProperty("jdbc.url");
    }

    public static ResultSet select(String what, String tableName)
            throws SQLException {

        request = getConnection().createStatement();

        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("SELECT ").append(what);
        sqlBuilder.append(" FROM ").append(tableName);

        sql = sqlBuilder.toString();
        return request.executeQuery(sql);
    }

    public static ResultSet select(String what, String tableName, String where)
            throws SQLException {
        request = getConnection().createStatement();

        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("SELECT ").append(what);
        sqlBuilder.append(" FROM ").append(tableName);
        sqlBuilder.append(" WHERE ").append(where);

        sql = sqlBuilder.toString();
        return request.executeQuery(sql);
    }

    /**
     * Handle INSERTion to the database
     * @param tableName
     * @param columnNames
     * @param values
     * @throws java.sql.SQLException
     */
    public static ResultSet insertInto(String tableName, String[] columnNames, String[] values)
            throws SQLException {

        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("INSERT INTO " + tableName);
        sqlBuilder.append("(");

        // Create list of columns
        for(int i = 0; i < columnNames.length; i++) {
            sqlBuilder.append(columnNames[i]);
            if(i < columnNames.length-1)
                sqlBuilder.append(", ");
        }
        sqlBuilder.append(") ");


        sqlBuilder.append("Values(");

        for(int i = 0; i < values.length; i++) {
            sqlBuilder.append("'" + values[i] + "'");

            if(i < values.length-1)
                sqlBuilder.append(", ");
        }
        sqlBuilder.append(") ");

        sql = sqlBuilder.toString();

        request = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        request.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        return request.getGeneratedKeys();
    }

    public static void update(String tableName, String[] columnNames, String[] values, String condition)
            throws SQLException {
        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("UPDATE ").append(tableName).append(" ");
        sqlBuilder.append("SET ");
        for(int i = 0; i < values.length; i++) {
            sqlBuilder.append(columnNames[i]).append(" = ");
            sqlBuilder.append("'" + values[i] + "'").append(" ");

            if(i < values.length-1)
                sqlBuilder.append(", ");
        }
        sqlBuilder.append("WHERE ").append(condition);

        sql = sqlBuilder.toString();

        request = getConnection().createStatement();
        request.execute(sql);
    }

    public static void delete(String tableName, String condition)
            throws SQLException {
        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("DELETE FROM ").append(tableName).append(" ");
        sqlBuilder.append("WHERE ").append(condition);

        sql = sqlBuilder.toString();
        request = getConnection().createStatement();
        request.execute(sql);
    }

    public static void clearConnection() {
        try {
            if (connect != null)
                connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (request != null)
                request.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        try {
            if (connect == null || connect.isClosed()) {
                connect = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connect;
    }
}