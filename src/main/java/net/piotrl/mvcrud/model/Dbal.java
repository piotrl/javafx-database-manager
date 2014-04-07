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

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static Connection connect;

    static {
        Properties props = new Properties();
        FileInputStream in = null;

        try {
            in = new FileInputStream("src/main/resources/database.properties");
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

       try {
           connect = getConnection();
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static ResultSet select(String what, String tableName)
            throws SQLException {
        Statement request = connect.createStatement();

        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("SELECT ").append(what);
        sqlBuilder.append(" FROM ").append(tableName);

        sql = sqlBuilder.toString();
        return request.executeQuery(sql);
    }

    public static ResultSet select(String what, String tableName, String where)
            throws SQLException {
        Statement request = connect.createStatement();

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

        Statement request = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int n = request.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        return request.getGeneratedKeys();

    }

    public static void update(String tableName, String[] columnNames, String[] values, String condition)
            throws SQLException {
        Statement request = connect.createStatement();

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
        request.execute(sql);
    }

    public static void delete(String tableName, String condition)
            throws SQLException {
        Statement request = connect.createStatement();

        String sql = "";
        StringBuilder sqlBuilder = new StringBuilder(sql);

        sqlBuilder.append("DELETE FROM ").append(tableName).append(" ");
        sqlBuilder.append("WHERE ").append(condition);

        sql = sqlBuilder.toString();
        request.execute(sql);
    }
}