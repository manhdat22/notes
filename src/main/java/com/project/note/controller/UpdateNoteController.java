package com.project.note.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import com.project.note.database.DatabaseHandler;
import com.project.note.helper.Shaker;
import com.project.note.model.Note;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class UpdateNoteController {
    DatabaseHandler databaseHandler = new DatabaseHandler();

    Calendar calendar = Calendar.getInstance();

    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXTextArea contentField;

    @FXML
    public JFXButton updateNoteButton;

    @FXML
    public Label saveAsLabel;

    @FXML
    void initialize() {

    }

    public void setTitleField(String title) {
        titleField.setText(title);
    }

    public String getTitle() {
        return this.titleField.getText().trim();
    }

    public void setUpdateContentField(String content) {
        this.contentField.setText(content);
    }

    public String getContent() {
        return this.contentField.getText().trim();
    }

    public void shakeForm() {
        Shaker titleFieldShaker = new Shaker(titleField);
        Shaker contentFieldShaker = new Shaker(contentField);
        contentFieldShaker.shake();
        titleFieldShaker.shake();
    }

    public void createNote(Note note) {
        if (getTitle().isBlank() || getContent().isBlank()) {
            shakeForm();
            return;
        }

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        note.setUserId(LoginController.currentUserId);
        note.setTitle(getTitle());
        note.setContent(getContent());
        note.setDatecreated(timestamp);

        databaseHandler.insertNote(note);
    }

    public void updateNote(Note note) {
        if (getTitle().isBlank() || getContent().isBlank()) {
            shakeForm();
            return;
        }

        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());

        try {
            databaseHandler.updateNote(timestamp, getContent(), getTitle(), note.getNoteId());
            updateNoteButton.getScene().getWindow().hide();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveNoteToFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Note As");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName(getTitle().toLowerCase().replace(" ", "-") + ".txt");

        File file = fileChooser.showSaveDialog(null);

        if (file == null)
            return;

        try {
            FileWriter writer = new FileWriter(file);

            writer.write(getContent());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
