package com.project.note.database;

import java.sql.*;

import com.project.note.model.Note;
import com.project.note.model.User;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public void updateNote(Timestamp datecreated, String description, String note, int noteId)
            throws SQLException, ClassNotFoundException {

        String query = "UPDATE " + Const.NOTES_TABLE + " SET " + Const.NOTES_DATE + "=?," + Const.NOTES_CONTENT + "=?,"
                + Const.NOTES_TITLE + "=? WHERE " + Const.NOTES_ID + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setTimestamp(1, datecreated);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, note);
        preparedStatement.setInt(4, noteId);
        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    // Delete Note
    public void deleteNote(int userId, int noteId) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM " + Const.NOTES_TABLE + " WHERE " + Const.USERS_ID + "=?" + " AND " + Const.NOTES_ID
                + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, noteId);
        preparedStatement.execute();
        preparedStatement.close();
    }

    // Write
    public void signUpUser(User user) {

        String insert = "INSERT INTO " + Const.USERS_TABLE + "(" + Const.USERS_FIRSTNAME + "," + Const.USERS_LASTNAME
                + "," + Const.USERS_USERNAME + "," + Const.USERS_PASSWORD + "," + Const.USERS_GENDER + ")"
                + "VALUES(?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getGender());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ResultSet getNotesByUser(int userId) {

        ResultSet resultNotes = null;

        String query = "SELECT * FROM " + Const.NOTES_TABLE + " WHERE " + Const.USERS_ID + "=?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

            preparedStatement.setInt(1, userId);

            resultNotes = preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resultNotes;
    }

    public ResultSet getUser(User user) {
        ResultSet resultSet = null;

        if (!user.getUserName().equals("") || !user.getPassword().equals("")) {
            String query = "SELECT * FROM " + Const.USERS_TABLE + " WHERE " + Const.USERS_USERNAME + "=?" + " AND "
                    + Const.USERS_PASSWORD + "=?";

            // select all from users where username="paulo" and password="password"

            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());

                resultSet = preparedStatement.executeQuery();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Please enter your credentials");

        }

        return resultSet;
    }

    public int getAllNotes(int userId) throws SQLException, ClassNotFoundException {

        String query = "SELECT COUNT(*) FROM " + Const.NOTES_TABLE + " WHERE " + Const.USERS_ID + "=?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setInt(1, userId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return resultSet.getInt(1);

    }

    public void insertNote(Note note) {

        String insert = "INSERT INTO " + Const.NOTES_TABLE + "(" + Const.USERS_ID + "," + Const.NOTES_DATE + ","
                + Const.NOTES_CONTENT + "," + Const.NOTES_TITLE + ")" + "VALUES(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            System.out.println("From DBHandler UserId: " + note.getUserId());

            preparedStatement.setInt(1, note.getUserId());
            preparedStatement.setTimestamp(2, note.getDatecreated());
            preparedStatement.setString(3, note.getContent());
            preparedStatement.setString(4, note.getTitle());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    // Read

    // Update

    // Delete

}
