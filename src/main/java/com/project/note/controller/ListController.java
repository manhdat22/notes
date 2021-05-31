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
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ListController {

    public static int userId;

    private String name;

    private String gender;

    @FXML
    private Label sceneTitle;

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

    @FXML
    void initialize() throws SQLException {
        sceneTitle.setText(getSceneTitle());

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
            try {
                addNewNote();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        refreshList();

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

    public void addNewNote() throws SQLException {
        Note newNote = new Note();

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
            updateNoteController.createNote(newNote);

            updateNoteController.updateNoteButton.getScene().getWindow().hide();
        });

        updateNoteController.saveAsLabel.setOnMouseClicked(event -> {
            updateNoteController.createNote(newNote);
            updateNoteController.saveNoteToFile(stage);

            updateNoteController.updateNoteButton.getScene().getWindow().hide();
        });

        stage.show();
    }

    private String getSceneTitle() throws SQLException {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        ResultSet userRow = databaseHandler.getUserById(LoginController.currentUserId);

        int counter = 0;
        String name = "";

        while (userRow.next()) {
            counter++;

            name = userRow.getString("firstname");
            gender = userRow.getString("gender");
            userId = userRow.getInt("userid");
        }

        if (counter == 1) {
            String title;

            if (gender == "female") {
                title = "Ms.";
            } else {
                title = "Mr.";
            }

            return title + name + "'s Notes";
        } else {
            return "Notes";
        }
    }
}
