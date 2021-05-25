package com.project.note.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.project.note.database.DatabaseHandler;
import com.project.note.model.Note;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ListController {

    public static int userId;

    @FXML
    private ImageView listRefreshButton;

    @FXML
    private JFXListView<Note> listNote;

    @FXML
    public JFXTextField listNoteField;

    @FXML
    public JFXTextField listContentField;

    @FXML
    public JFXButton listSaveNoteButton;

    @FXML
    public JFXButton logOutButton;

    @FXML
    public JFXButton addButton;

    private ObservableList<Note> notes;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() throws SQLException {
        System.out.println("initialize called");

        logOutButton.setOnAction(event -> {
            logOutButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/project/note/view/login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            stage.show();
        });

        addButton.setOnAction(event -> {
            addNewNote();
        });

        TimerTask note = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        refreshList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        new Timer().scheduleAtFixedRate(note, 0, 2000);
    }

    public void refreshList() throws SQLException {

        System.out.println("Current list of User#" + LoginController.currentUserId);

        notes = FXCollections.observableArrayList();

        DatabaseHandler databaseHandler = new DatabaseHandler();

        ResultSet resultSet = databaseHandler.getNotesByUser(LoginController.currentUserId);

        while (resultSet.next()) {
            Note note = new Note();
            note.setNoteId(resultSet.getInt("noteid"));
            note.setTitle(resultSet.getString("title"));
            note.setDatecreated(resultSet.getTimestamp("datecreated"));
            note.setContent(resultSet.getString("content"));

            notes.addAll(note);
        }

        listNote.setItems(notes);
        listNote.setCellFactory(CellController -> new CellController());
    }

    public void addNewNote() {
        Note newNote = new Note();

        DatabaseHandler databaseHandler = new DatabaseHandler();

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/com/project/note/view/updateNoteForm.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);

        UpdateNoteController updateNoteController = loader.getController();
        updateNoteController.updateNoteButton.setOnAction(event -> {

            Calendar calendar = Calendar.getInstance();

            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

            System.out.println("taskid " + newNote.getNoteId());

            newNote.setUserId(LoginController.currentUserId);
            newNote.setTitle(updateNoteController.getTitle());
            newNote.setContent(updateNoteController.getContent());
            newNote.setDatecreated(timestamp);

            databaseHandler.insertNote(newNote);

            updateNoteController.updateNoteButton.getScene().getWindow().hide();

        });

        stage.show();

    }

}
