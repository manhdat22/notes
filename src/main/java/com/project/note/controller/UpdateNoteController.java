package com.project.note.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class UpdateNoteController {

    @FXML
    private JFXTextField titleField;

    @FXML
    private JFXTextArea updateContentField;

    @FXML
    public JFXButton updateNoteButton;

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
        this.updateContentField.setText(content);
    }

    public String getContent() {
        return this.updateContentField.getText().trim();
    }

    public void refreshList() throws SQLException {

        System.out.println("Calling refresh list");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/project/note/view/list.fxml"));

        try {
            loader.load();

            ListController listController = loader.getController();
            listController.refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
