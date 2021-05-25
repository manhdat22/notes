package com.project.note.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.project.note.database.DatabaseHandler;
import com.project.note.model.Note;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

public class AddItemFormController {

    private int userId;

    private DatabaseHandler databaseHandler;

    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXTextField contentField;

    @FXML
    private JFXButton saveNoteButton;

    @FXML
    private Label successLabel;

    @FXML
    private JFXButton todosButton;

    @FXML
    void initialize() {

        databaseHandler = new DatabaseHandler();
        Note task = new Note();

        saveNoteButton.setOnAction(event -> {

            Calendar calendar = Calendar.getInstance();

            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

            String noteTitle = titleField.getText().trim();
            String noteContent = contentField.getText().trim();

            if (!noteTitle.equals("") || !noteContent.equals("")) {

                System.out.println("User Id: " + AddItemController.userId);

                task.setUserId(AddItemController.userId);
                task.setDatecreated(timestamp);
                task.setContent(noteContent);
                task.setTitle(noteTitle);

                databaseHandler.insertNote(task);

                successLabel.setVisible(true);

                todosButton.setVisible(true);
                int taskNumber = 0;
                try {
                    taskNumber = databaseHandler.getAllNotes(AddItemController.userId);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                todosButton.setText("My TODO's: " + "(" + taskNumber + ")");

                titleField.setText("");
                contentField.setText("");

                todosButton.setOnAction(event1 -> {
                    // send users to the list screen

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/com/project/note/view/list.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);

                    stage.showAndWait();

                });

                // System.out.println("Note Added Successfully!");

            } else {
                System.out.println("Nothing added!");

            }

        });

    }

    public int getUserId() {
        System.out.println("from getUserId() " + userId);

        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("From setUserId " + this.userId);
    }
}
