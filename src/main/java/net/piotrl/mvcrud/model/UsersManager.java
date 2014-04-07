package net.piotrl.mvcrud.model;

import net.piotrl.mvcrud.model.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Communication with Database in Users contex
 */
public class UsersManager {
    protected String tableName = "users";
    protected String columnNames[] = { "name", "registered" };
    protected String[] values = new String[columnNames.length];

    /**
     * Read data
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSet getAll()
            throws SQLException {
        return Dbal.select("*", tableName);
    }

    public ResultSet getEqual(String column, String condtition)
            throws SQLException {
        return Dbal.select("*", tableName, column + " = " + condtition);
    }

    /**
     * Create data into db
     * @param user
     */
    public void add(User user)
            throws SQLException {

        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String registered = sqlDateFormat.format(user.getRegisterDate());
        ResultSet insertedData;

        values[0] = user.getName();
        values[1] = registered;

        insertedData = Dbal.insertInto(tableName, columnNames, values);

        if(insertedData.next()) {
            // 1 is id column in the users table; set it to the user object
            user.setId(insertedData.getInt(1));
        }
    }

    public void remove(String columnName, String condition)
            throws SQLException {
        Dbal.delete(tableName, columnName + " = " + condition);
    }

    public void remove(User user)
            throws SQLException {
        Dbal.delete(tableName, "id" + " = '" + user.getId() + "'");
    }

    public void edit(User user)
            throws SQLException {

        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String registered = sqlDateFormat.format(user.getRegisterDate());

        values[0] = user.getName();
        values[1] = registered;

        String condition = "id = " + user.getId();

        try {
            Dbal.update(tableName, columnNames, values, condition);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
